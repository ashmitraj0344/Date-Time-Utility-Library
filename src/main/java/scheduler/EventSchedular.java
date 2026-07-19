package scheduler;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<String> events;

    public Scheduler() {
        this.events = new ArrayList<>();
    }

    public void addEvent(String eventName) {
        events.add(eventName);
        System.out.println("Event added: " + eventName);
    }

    public List<String> getEvents() {
        return events;
    }
}
