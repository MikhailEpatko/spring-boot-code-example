package ru.emi.util;

import java.time.LocalDate;
import java.time.ZoneId;

public class MyDate {
    public static LocalDate now() {
        return LocalDate.now(ZoneId.of("Europe/Moscow"));
    }
}
