package softuni.exam.models.entity;

import java.time.DayOfWeek;

public enum  DaysOfWeek {
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static boolean contains(String value) {
        try {
            DayOfWeek.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}