package com.connexal.mcdlmi.components.totalitarian1984.listeners;

import com.connexal.mcdlmi.components.totalitarian1984.Abstract1984Listener;
import com.connexal.mcdlmi.components.totalitarian1984.Logging1984;
import com.connexal.mcdlmi.components.totalitarian1984.utils.StringConversion;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.InventoryHolder;

public class BucketListener extends Abstract1984Listener {
    public BucketListener(Logging1984 logger) {
        super("bucket", logger);
    }

    @EventHandler
    public void playerBucketFill(PlayerBucketFillEvent event) {
        this.log("Filled at " + StringConversion.fromLocation(event.getBlockClicked().getLocation()) + " with " + StringConversion.fromItemStack(event.getItemStack()), event.getPlayer());
    }

    @EventHandler
    public void playerBucketEmpty(PlayerBucketEmptyEvent event) {
        this.log("Emptied at " + StringConversion.fromLocation(event.getBlockClicked().getLocation()) + " with " + event.getBucket().name(), event.getPlayer());
    }
}
