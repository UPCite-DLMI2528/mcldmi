package com.connexal.mcdlmi.components.misc;

import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.google.auto.service.AutoService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AutoService(DLMIComponent.class)
public class ConnectionMessages implements DLMIComponent, Listener {
    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("connection-messages");
    }

    @Override
    public void enable(ComponentContext context) {
        context.registries().events().register(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(Component.text(player.getName() + " joined the server!", NamedTextColor.YELLOW));

        // Send a welcome message
        event.getPlayer().sendMessage(Component.text("Welcome the DLMI Minecraft server! Enjoy your stay!", NamedTextColor.AQUA));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage(Component.text(player.getName() + " left the server", NamedTextColor.YELLOW));
    }
}
