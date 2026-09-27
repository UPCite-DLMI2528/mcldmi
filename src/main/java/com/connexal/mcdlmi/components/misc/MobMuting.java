package com.connexal.mcdlmi.components.misc;

import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.google.auto.service.AutoService;
import io.papermc.paper.event.player.PlayerNameEntityEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AutoService(DLMIComponent.class)
public class MobMuting implements DLMIComponent, Listener {
    private static final String MUTED_NAME = "Censored";

    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("mob-muting");
    }

    @Override
    public void enable(ComponentContext context) {
        context.registries().events().register(this);
    }

    @EventHandler
    public void onNameChanged(PlayerNameEntityEvent event) {
        LivingEntity mob = event.getEntity();
        Player player = event.getPlayer();

        TextComponent newName = (TextComponent) event.getName();
        if (newName == null) {
            // Ignore if the mob has no name
            return;
        }

        // A single boolean won't suffice because these cases are not exhaustive
        if (!mob.getName().equals(MUTED_NAME) && newName.content().equals(MUTED_NAME)) {
            this.notifyPlayer(player, true);
            mob.setSilent(true);
        } else if (mob.getName().equals(MUTED_NAME) && !newName.content().equals(MUTED_NAME)) {
            this.notifyPlayer(player, false);
            mob.setSilent(false);
        }
    }

    private void notifyPlayer(Player player, String message) {
        player.sendMessage(Component.text(message, NamedTextColor.AQUA));
    }

    private void notifyPlayer(Player player, boolean muted) {
        if (muted) {
            this.notifyPlayer(player, "You have muted this mob: it will no longer make sounds. Rename it to unmute it.");
        } else {
            this.notifyPlayer(player, "You have unmuted this mob. It will now make sounds again!");
        }
    }
}
