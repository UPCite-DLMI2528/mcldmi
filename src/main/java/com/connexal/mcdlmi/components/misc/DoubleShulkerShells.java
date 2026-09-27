package com.connexal.mcdlmi.components.misc;

import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.google.auto.service.AutoService;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

@AutoService(DLMIComponent.class)
public class DoubleShulkerShells implements DLMIComponent, Listener {
    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("double-shulker-shells");
    }

    @Override
    public void enable(ComponentContext context) {
        context.registries().events().register(this);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.SHULKER) {
            event.getDrops().clear();
            ItemStack shulkerStack = new ItemStack(Material.SHULKER_SHELL, 2);
            event.getEntity().getWorld().dropItemNaturally(entity.getLocation(), shulkerStack);
        }
    }
}
