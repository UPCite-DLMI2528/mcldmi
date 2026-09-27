package com.connexal.mcdlmi.components.misc;

import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@AutoService(DLMIComponent.class)
public class CropProtection implements DLMIComponent, Listener {
    private final ImmutableList<Material> farmlandBlocks = ImmutableList.of(Material.FARMLAND);

    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("crop-protection");
    }

    @Override
    public void enable(ComponentContext context) {
        context.registries().events().register(this);
    }

    @EventHandler
    public void onMobTrample(EntityInteractEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }

        if (this.farmlandBlocks.contains(event.getBlock().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTrampleCrop(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock() != null && this.farmlandBlocks.contains(event.getClickedBlock().getType())) {
            event.setCancelled(true);
        }
    }
}
