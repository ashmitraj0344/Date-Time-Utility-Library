package utility;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtility {
    private static final DateTimeFormatter TIME_24H = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter TIME_12H = DateTimeFormatter.ofPattern("hh:mm a");

    public static String getCurrentTime(String pattern) {
        try {
            return LocalTime.now().format(DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return LocalTime.now().format(TIME_12H);
        }
    }

    public static String getTimeDifference(String t1Str, String t2Str) {
        try {
            // Assume 24H input HH:mm
            LocalTime t1 = LocalTime.parse(t1Str, TIME_24H);
            LocalTime t2 = LocalTime.parse(t2Str, TIME_24H);
            long diffSeconds = ChronoUnit.SECONDS.between(t1, t2);
            long absDiff = Math.abs(diffSeconds);
            long hours = absDiff / 3600;
            long minutes = (absDiff % 3600) / 60;
            long seconds = absDiff % 60;
            return String.format("%02d hours, %02d minutes, %02d seconds", hours, minutes, seconds);
        } catch (Exception e) {
            return "Invalid Time (use HH:mm)";
        }
    }

    public static String convertTimeFormat(String timeStr) {
        try {
            timeStr = timeStr.trim();
            if (timeStr.toLowerCase().contains("am") || timeStr.toLowerCase().contains("pm")) {
                // 12h to 24h
                LocalTime t = LocalTime.parse(timeStr, TIME_12H);
                return t.format(TIME_24H);
            } else {
                // 24h to 12h
                LocalTime t = LocalTime.parse(timeStr, TIME_24H);
                return t.format(TIME_12H);
            }
        } catch (Exception e) {
            return "Invalid format";
        }
    }

    public static String addSubtractMinutes(String timeStr, int minutes) {
        try {
            LocalTime t = LocalTime.parse(timeStr, TIME_24H);
            LocalTime result = t.plusMinutes(minutes);
            return result.format(TIME_24H);
        } catch (Exception e) {
            return "Invalid Time (use HH:mm)";
        }
    }
}
