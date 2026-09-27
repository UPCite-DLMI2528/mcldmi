package com.connexal.mcdlmi.components.misc;

import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.google.auto.service.AutoService;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

@AutoService(DLMIComponent.class)
public class InvisibleItemFrames implements DLMIComponent, Listener {
    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("invisible-item-frames");
    }

    @Override
    public void enable(ComponentContext context) {
        context.registries().events().register(this);
    }

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event) {
        if (event.getHand() == EquipmentSlot.HAND && event.getRightClicked() instanceof ItemFrame entity) {
            if (event.getPlayer().isSneaking() && entity.getItem().getType() == Material.AIR) {
                entity.setVisible(!entity.isVisible());
            }
        }
    }
}
