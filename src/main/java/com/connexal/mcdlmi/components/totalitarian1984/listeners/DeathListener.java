package com.connexal.mcdlmi.components.totalitarian1984.listeners;

import com.connexal.mcdlmi.components.totalitarian1984.Abstract1984Listener;
import com.connexal.mcdlmi.components.totalitarian1984.Logging1984;
import com.connexal.mcdlmi.components.totalitarian1984.utils.StringConversion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener extends Abstract1984Listener {
    public DeathListener(Logging1984 logger) {
        super("death", logger);
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {
        this.log("Cause: " + StringConversion.fromComponent(event.deathMessage()) + "; At " + StringConversion.fromLocation(event.getPlayer().getLocation()), event.getEntity());
    }
}
