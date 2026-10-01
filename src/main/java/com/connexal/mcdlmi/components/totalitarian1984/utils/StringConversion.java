package com.connexal.mcdlmi.components.totalitarian1984.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public final class StringConversion {
    public static String fromComponent(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String fromLocation(Location location) {
        return location.getWorld().getName() + ": " + location.getX() + " " + location.getY() + " " + location.getZ();
    }

    public static String fromItemStack(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return "null";
        }
        return item.getType().name() + " x" + item.getAmount();
    }

    public static String fromItem(Item item) {
        if (item == null) {
            return "null";
        }
        return fromItemStack(item.getItemStack());
    }
}
