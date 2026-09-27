package com.connexal.mcdlmi.components.misc;

import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.google.auto.service.AutoService;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

@AutoService(DLMIComponent.class)
public class SkeletonBowBreak implements DLMIComponent, Listener {
    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("skeleton-bow-break");
    }

    @Override
    public void enable(ComponentContext context) {
        context.registries().events().register(this);
    }

    @EventHandler
    public void borisSkeletonHack(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof AbstractSkeleton skeleton)) {
            return;
        }

        ItemStack bow = event.getBow();
        if (bow == null || bow.getType() != Material.BOW) {
            return;
        }

        Integer damageValue = bow.getData(DataComponentTypes.DAMAGE);
        if (damageValue == null) {
            return;
        }
        damageValue += 3; // Make the bow take damage

        if (damageValue > Material.BOW.getMaxDurability()) {
            skeleton.getEquipment().setItemInMainHand(null, true);
            skeleton.getWorld().playSound(skeleton.getLocation(), Sound.ENTITY_SKELETON_DEATH, 1.0F, 1.0F);
        } else {
            bow.setData(DataComponentTypes.DAMAGE, damageValue);
            skeleton.getEquipment().setItemInMainHand(bow, true);
        }
    }
}
