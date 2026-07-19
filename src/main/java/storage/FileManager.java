package storage;

import model.Event;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "events_data.txt";
    private static final String DELIMITER = "\\|\\|\\|";
    private static final String WRITE_DELIMITER = "|||";

    public static synchronized void saveEvents(List<Event> events) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Event e : events) {
                // Replace any occurrence of delimiter in inputs to prevent file corruption
                String id = sanitize(e.getId());
                String title = sanitize(e.getTitle());
                String desc = sanitize(e.getDescription());
                String date = sanitize(e.getDate());
                String time = sanitize(e.getTime());
                String loc = sanitize(e.getLocation());
                String priority = sanitize(e.getPriority());
                String reminder = String.valueOf(e.isReminder());
                String reminderTime = sanitize(e.getReminderTime());

                String line = id + WRITE_DELIMITER +
                             title + WRITE_DELIMITER +
                             desc + WRITE_DELIMITER +
                             date + WRITE_DELIMITER +
                             time + WRITE_DELIMITER +
                             loc + WRITE_DELIMITER +
                             priority + WRITE_DELIMITER +
                             reminder + WRITE_DELIMITER +
                             reminderTime;
                writer.println(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized List<Event> loadEvents() {
        List<Event> events = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return events;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length >= 9) {
                    Event e = new Event(
                        desanitize(parts[0]),
                        desanitize(parts[1]),
                        desanitize(parts[2]),
                        desanitize(parts[3]),
                        desanitize(parts[4]),
                        desanitize(parts[5]),
                        desanitize(parts[6]),
                        Boolean.parseBoolean(parts[7]),
                        desanitize(parts[8])
                    );
                    events.add(e);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return events;
    }

    private static String sanitize(String input) {
        if (input == null) return "";
        // Replace newlines and delimiter characters to preserve file format
        return input.replace("\n", "\\n").replace("\r", "").replace("|||", " ");
    }

    private static String desanitize(String input) {
        if (input == null) return "";
        return input.replace("\\n", "\n");
    }
}
