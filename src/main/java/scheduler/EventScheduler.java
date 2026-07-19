package scheduler;

import model.Event;
import storage.FileManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class EventScheduler {
    private List<Event> events;
    private final List<ReminderListener> reminderListeners;
    private final Set<String> triggeredReminders;
    private Thread reminderThread;
    private boolean running = true;

    public interface ReminderListener {
        void onEventReminder(Event event);
    }

    public EventScheduler() {
        this.events = FileManager.loadEvents();
        this.reminderListeners = new ArrayList<>();
        this.triggeredReminders = new HashSet<>();
        startReminderCheckLoop();
    }

    public synchronized List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }

    public synchronized void addEvent(Event event) {
        events.add(event);
        FileManager.saveEvents(events);
    }

    public synchronized void updateEvent(Event updatedEvent) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId().equals(updatedEvent.getId())) {
                events.set(i, updatedEvent);
                break;
            }
        }
        FileManager.saveEvents(events);
    }

    public synchronized void deleteEvent(String id) {
        events.removeIf(e -> e.getId().equals(id));
        triggeredReminders.remove(id);
        FileManager.saveEvents(events);
    }

    public synchronized void addReminderListener(ReminderListener listener) {
        reminderListeners.add(listener);
    }

    public List<Event> searchEvents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllEvents();
        }
        String lowerQuery = query.toLowerCase();
        List<Event> results = new ArrayList<>();
        for (Event e : getAllEvents()) {
            if ((e.getTitle() != null && e.getTitle().toLowerCase().contains(lowerQuery)) ||
                (e.getDescription() != null && e.getDescription().toLowerCase().contains(lowerQuery)) ||
                (e.getLocation() != null && e.getLocation().toLowerCase().contains(lowerQuery))) {
                results.add(e);
            }
        }
        return results;
    }

    public List<Event> getEventsForDate(String dateStr) {
        List<Event> results = new ArrayList<>();
        for (Event e : getAllEvents()) {
            if (e.getDate().equals(dateStr)) {
                results.add(e);
            }
        }
        // Sort by time
        results.sort(Comparator.comparing(Event::getTime));
        return results;
    }

    public void startReminderCheckLoop() {
        reminderThread = new Thread(() -> {
            while (running) {
                try {
                    checkReminders();
                    // Check every 10 seconds
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        reminderThread.setDaemon(true);
        reminderThread.start();
    }

    public void stop() {
        this.running = false;
        if (reminderThread != null) {
            reminderThread.interrupt();
        }
    }

    private synchronized void checkReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> currentEvents = new ArrayList<>(events);

        for (Event e : currentEvents) {
            if (!e.isReminder() || triggeredReminders.contains(e.getId())) {
                continue;
            }

            try {
                LocalDate eventDate = LocalDate.parse(e.getDate());
                LocalTime eventTime = LocalTime.parse(e.getTime());
                LocalDateTime eventDateTime = LocalDateTime.of(eventDate, eventTime);

                long minutesBefore = 0;
                String remTime = e.getReminderTime();
                if (remTime.contains("15 min")) {
                    minutesBefore = 15;
                } else if (remTime.contains("30 min")) {
                    minutesBefore = 30;
                } else if (remTime.contains("1 hour")) {
                    minutesBefore = 60;
                } else if (remTime.contains("1 day")) {
                    minutesBefore = 1440;
                }

                LocalDateTime alertTime = eventDateTime.minusMinutes(minutesBefore);

                // If current time is past the alert time, but not more than 2 hours past the event time
                if (now.isAfter(alertTime) && now.isBefore(eventDateTime.plusHours(2))) {
                    triggeredReminders.add(e.getId());
                    notifyReminder(e);
                }
            } catch (Exception ex) {
                // Ignore parsing errors for individual events
            }
        }
    }

    private void notifyReminder(Event e) {
        for (ReminderListener listener : reminderListeners) {
            listener.onEventReminder(e);
        }
    }
}
