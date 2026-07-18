
package utility;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AgeCalculator {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static class AgeResult {
        public int years;
        public int months;
        public int days;
        public long totalDays;
        public long totalWeeks;
        public long totalHours;
        public long totalMinutes;
        public long totalSeconds;

        @Override
        public String toString() {
            return years + " Years, " + months + " Months, " + days + " Days";
        }
    }

    public static AgeResult calculateAge(String dobStr) {
        try {
            LocalDate dob = LocalDate.parse(dobStr, DATE_FORMAT);
            LocalDate today = LocalDate.now();
            if (dob.isAfter(today)) {
                return null;
            }

            Period period = Period.between(dob, today);
            AgeResult result = new AgeResult();
            result.years = period.getYears();
            result.months = period.getMonths();
            result.days = period.getDays();

            // Total statistics
            result.totalDays = ChronoUnit.DAYS.between(dob, today);
            result.totalWeeks = result.totalDays / 7;
            result.totalHours = result.totalDays * 24;
            result.totalMinutes = result.totalHours * 60;
            result.totalSeconds = result.totalMinutes * 60;

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getNextBirthdayCountdown(String dobStr) {
        try {
            LocalDate dob = LocalDate.parse(dobStr, DATE_FORMAT);
            LocalDate today = LocalDate.now();

            // Calculate next birthday date
            LocalDate nextBday = dob.withYear(today.getYear());
            if (nextBday.isBefore(today) || nextBday.isEqual(today)) {
                nextBday = nextBday.plusYears(1);
            }

            long daysLeft = ChronoUnit.DAYS.between(today, nextBday);
            Period p = Period.between(today, nextBday);
            return String.format("%d Months, %d Days (%d total days remaining)", p.getMonths(), p.getDays(), daysLeft);
        } catch (Exception e) {
            return "Invalid Date";
        }
    }
}
