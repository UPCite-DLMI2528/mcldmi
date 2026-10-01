package com.connexal.mcdlmi.components.totalitarian1984;

import com.connexal.mcdlmi.components.totalitarian1984.utils.LogEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class Abstract1984Listener implements Listener {
    private final String category;
    private final Logging1984 logger;

    public Abstract1984Listener(String category, Logging1984 logger) {
        this.category = category;
        this.logger = logger;
    }

    protected void log(String message, UUID uuid) {
        this.logger.add(new LogEntry(ZonedDateTime.now(), this.category, uuid, message));
    }

    protected void log(String message, Player player) {
        this.log(message, player.getUniqueId());
    }
}
