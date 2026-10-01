package com.connexal.mcdlmi.components.totalitarian1984.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record LogEntry(ZonedDateTime timestamp, String category, UUID uuid, String message) {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String date() {
        return timestamp.format(dateFormatter);
    }

    public String time() {
        return timestamp.format(timeFormatter);
    }
}
