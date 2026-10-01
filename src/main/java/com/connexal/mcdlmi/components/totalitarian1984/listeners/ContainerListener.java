package com.connexal.mcdlmi.components.totalitarian1984.listeners;

import com.connexal.mcdlmi.components.totalitarian1984.Abstract1984Listener;
import com.connexal.mcdlmi.components.totalitarian1984.Logging1984;
import com.connexal.mcdlmi.components.totalitarian1984.utils.StringConversion;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ContainerListener extends Abstract1984Listener {
    private final ImmutableList<InventoryType> containerTypes = ImmutableList.of(
            InventoryType.CHEST,
            InventoryType.ENDER_CHEST,
            InventoryType.BARREL,
            InventoryType.SHULKER_BOX
    );

    public ContainerListener(Logging1984 logger) {
        super("container", logger);
    }

    @EventHandler
    public void inventoryMoveItem(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }

        if (containerTypes.contains(event.getInventory().getType())) {
            if (event.getCursor().getType() == Material.AIR && event.getCurrentItem() == null && event.getCurrentItem().getType() == Material.AIR) {
                return;
            }

            Location location;
            String locType;

            InventoryHolder holder = event.getInventory().getHolder();
            if (holder instanceof BlockInventoryHolder blockHolder) {
                location = blockHolder.getBlock().getLocation();
                locType = "Block";
            } else {
                location = event.getWhoClicked().getLocation();
                locType = "Player";
            }

            this.log("Type: " + event.getAction().name() + "; Container: " + event.getClickedInventory().getType().name() + "; Item 1: " + StringConversion.fromItemStack(event.getCurrentItem()) + "; Item 2: " + StringConversion.fromItemStack(event.getCursor()) + "; " + locType + " Location: " + StringConversion.fromLocation(location), event.getWhoClicked().getUniqueId());
        }
    }
}
