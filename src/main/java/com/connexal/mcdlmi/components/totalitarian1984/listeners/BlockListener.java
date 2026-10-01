package com.connexal.mcdlmi.components.totalitarian1984.listeners;

import com.connexal.mcdlmi.components.totalitarian1984.Abstract1984Listener;
import com.connexal.mcdlmi.components.totalitarian1984.Logging1984;
import com.connexal.mcdlmi.components.totalitarian1984.utils.StringConversion;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BlockListener extends Abstract1984Listener {
    public BlockListener(Logging1984 logger) {
        super("block", logger);
    }

    @EventHandler
    public void playerBreakBlock(BlockBreakEvent event) {
        this.log("Break: " + event.getBlock().getType().name() + " at " + StringConversion.fromLocation(event.getBlock().getLocation()), event.getPlayer());
    }

    @EventHandler
    public void playerPlaceBlock(BlockPlaceEvent event) {
        this.log("Place: " + event.getBlock().getType().name() + " at " + StringConversion.fromLocation(event.getBlock().getLocation()), event.getPlayer());
    }
}
