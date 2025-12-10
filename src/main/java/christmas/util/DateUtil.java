package christmas.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public final class DateUtil {
    public static LocalDate getLocalDateFrom(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public static boolean isWeekend(int year, int month, int day) {
        LocalDate localDate = getLocalDateFrom(year, month, day);
        DayOfWeek  dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek.equals(DayOfWeek.FRIDAY) ||  dayOfWeek.equals(DayOfWeek.SATURDAY);
    }

    public static boolean isSunday(int year, int month, int day) {
        LocalDate localDate = getLocalDateFrom(year, month, day);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek.equals(DayOfWeek.SUNDAY);
    }

    public static boolean isChristmas(int month, int day) {
        int christmasMonth = 12;
        int christmasDay = 25;
        return month == christmasMonth &&  day == christmasDay;
    }

    public static boolean isSpecialDay(int year, int month, int day) {
        return isSunday(year, month, day) || isChristmas(month, day);
    }
}
