package com.connexal.mcdlmi.components.totalitarian1984.listeners;

import com.connexal.mcdlmi.components.totalitarian1984.Abstract1984Listener;
import com.connexal.mcdlmi.components.totalitarian1984.Logging1984;
import com.connexal.mcdlmi.components.totalitarian1984.utils.StringConversion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemListener extends Abstract1984Listener {
    public ItemListener(Logging1984 logger) {
        super("item", logger);
    }

    @EventHandler
    public void playerPickupItem(PlayerAttemptPickupItemEvent event) {
        this.log("Picked up " + StringConversion.fromItem(event.getItem()) + " at " + StringConversion.fromLocation(event.getItem().getLocation()), event.getPlayer());
    }

    @EventHandler
    public void playerThrowItem(PlayerDropItemEvent event) {
        this.log("Dropped " + event.getItemDrop().getItemStack().getType().name() + " x" + event.getItemDrop().getItemStack().getAmount() + " at " + StringConversion.fromLocation(event.getItemDrop().getLocation()), event.getPlayer());
    }
}
