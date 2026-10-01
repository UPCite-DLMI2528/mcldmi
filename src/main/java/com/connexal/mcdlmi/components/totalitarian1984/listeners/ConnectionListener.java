package com.connexal.mcdlmi.components.totalitarian1984.listeners;

import com.connexal.mcdlmi.components.totalitarian1984.Abstract1984Listener;
import com.connexal.mcdlmi.components.totalitarian1984.Logging1984;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener extends Abstract1984Listener {
    public ConnectionListener(Logging1984 logger) {
        super("connection", logger);
    }

    @EventHandler
    public void playerJoined(PlayerJoinEvent event) {
        this.log("Joined the server", event.getPlayer());
    }

    @EventHandler
    public void playerLeft(PlayerQuitEvent event) {
        this.log("Left the server: " + event.getReason().name(), event.getPlayer());
    }

    @EventHandler
    public void playerKicked(PlayerKickEvent event) {
        this.log("Kicked from the server: " + event.getCause().name(), event.getPlayer());
    }
}
