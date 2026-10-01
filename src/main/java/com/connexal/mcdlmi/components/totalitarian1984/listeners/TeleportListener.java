package com.connexal.mcdlmi.components.totalitarian1984.listeners;

import com.connexal.mcdlmi.components.totalitarian1984.Abstract1984Listener;
import com.connexal.mcdlmi.components.totalitarian1984.Logging1984;
import com.connexal.mcdlmi.components.totalitarian1984.utils.StringConversion;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener extends Abstract1984Listener {
    public TeleportListener(Logging1984 logger) {
        super("teleportation", logger);
    }

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event) {
        this.log("From: " + StringConversion.fromLocation(event.getFrom()) + "; To: " + StringConversion.fromLocation(event.getTo()), event.getPlayer());
    }
}
