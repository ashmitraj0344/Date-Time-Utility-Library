package utility;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DateUtility {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getCurrentDate(String pattern) {
        try {
            return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return LocalDate.now().format(ISO);
        }
    }

    public static long getDaysBetween(String date1Str, String date2Str) {
        try {
            LocalDate d1 = LocalDate.parse(date1Str, ISO);
            LocalDate d2 = LocalDate.parse(date2Str, ISO);
            return ChronoUnit.DAYS.between(d1, d2);
        } catch (Exception e) {
            return Long.MIN_VALUE; // sentinel for "invalid"
        }
    }

    public static String getReadableDifference(String date1Str, String date2Str) {
        try {
            LocalDate d1 = LocalDate.parse(date1Str, ISO);
            LocalDate d2 = LocalDate.parse(date2Str, ISO);

            // Make sure d1 <= d2 for the Period calculation
            boolean swapped = d1.isAfter(d2);
            if (swapped) {
                LocalDate tmp = d1;
                d1 = d2;
                d2 = tmp;
            }

            java.time.Period p = java.time.Period.between(d1, d2);
            String result = String.format("%d year%s, %d month%s, %d day%s",
                    p.getYears(),   p.getYears()   == 1 ? "" : "s",
                    p.getMonths(),  p.getMonths()  == 1 ? "" : "s",
                    p.getDays(),    p.getDays()    == 1 ? "" : "s");
            return swapped ? result + " (dates swapped)" : result;
        } catch (Exception e) {
            return "Invalid date(s) — use yyyy-MM-dd";
        }
    }

    public static String formatDate(String isoDateStr, String targetPattern) {
        try {
            LocalDate d = LocalDate.parse(isoDateStr, ISO);
            return d.format(DateTimeFormatter.ofPattern(targetPattern));
        } catch (Exception e) {
            return "Invalid input";
        }
    }

    public static String toISO(String dateStr, String sourcePattern) {
        try {
            LocalDate d = LocalDate.parse(dateStr.trim(),
                    DateTimeFormatter.ofPattern(sourcePattern));
            return d.format(ISO);
        } catch (Exception e) {
            return "Invalid input";
        }
    }

    public static String getDayOfWeek(String isoDateStr) {
        try {
            return LocalDate.parse(isoDateStr, ISO)
                    .getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        } catch (Exception e) {
            return "Invalid date";
        }
    }

    public static int getDaysInMonth(String isoDateStr) {
        try {
            return LocalDate.parse(isoDateStr, ISO)
                    .getMonth()
                    .length(LocalDate.parse(isoDateStr, ISO).isLeapYear());
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean isLeapYear(String isoDateStr) {
        try {
            return LocalDate.parse(isoDateStr, ISO).isLeapYear();
        } catch (Exception e) {
            return false;
        }
    }

    public static String addDays(String isoDateStr, long days) {
        try {
            return LocalDate.parse(isoDateStr, ISO).plusDays(days).format(ISO);
        } catch (Exception e) {
            return "Invalid date";
        }
    }

    public static String addMonths(String isoDateStr, long months) {
        try {
            return LocalDate.parse(isoDateStr, ISO).plusMonths(months).format(ISO);
        } catch (Exception e) {
            return "Invalid date";
        }
    }

    public static String addYears(String isoDateStr, long years) {
        try {
            return LocalDate.parse(isoDateStr, ISO).plusYears(years).format(ISO);
        } catch (Exception e) {
            return "Invalid date";
        }
    }

    public static boolean isValidDate(String isoDateStr) {
        try {
            LocalDate.parse(isoDateStr, ISO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
