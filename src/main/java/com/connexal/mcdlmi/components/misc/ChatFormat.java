package com.connexal.mcdlmi.components.misc;

import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.google.auto.service.AutoService;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;
import java.util.logging.Logger;

@AutoService(DLMIComponent.class)
public class ChatFormat implements DLMIComponent, Listener {
    private Server server = null;

    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("chat-format");
    }

    @Override
    public void enable(ComponentContext context) {
        this.server = context.server();

        context.registries().events().register(this);
    }

    /**
     * This function serves two purposes:
     * 1. It formats the chat message to include the player's display name and the message
     * 2. It strips away any signatures from the message, making all chat messages appear as if they were sent by the
     *    server. This is done to remove chat reporting features.
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChatEvent(AsyncChatEvent event) {
        event.setCancelled(true);

        // Send the message to all players
        Component message = event.getPlayer().displayName().append(Component.text(": ", NamedTextColor.WHITE)).append(event.message());
        for (Player player : this.server.getOnlinePlayers()) {
            player.sendMessage(message);
        }

        // Log to console
        Logger.getLogger("Minecraft").info("[CHAT]: " + PlainTextComponentSerializer.plainText().serialize(message));
    }

    /**
     * This function intercepts the /msg command and formats it better than the default implementation.
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase(Locale.ROOT).startsWith("/msg")) {
            event.setCancelled(true);

            String[] args = event.getMessage().split(" ");
            if (args.length < 3) {
                event.getPlayer().sendMessage(Component.text("Usage: /msg <player> <message>", NamedTextColor.RED));
                return;
            }

            Player recipient = this.server.getPlayer(event.getMessage().split(" ")[1]);
            if (recipient == null) {
                event.getPlayer().sendMessage(Component.text("That player is not online!", NamedTextColor.RED));
                return;
            }

            String message = event.getMessage().substring(event.getMessage().indexOf(args[2]));
            event.getPlayer().sendMessage(Component.text("To ", NamedTextColor.LIGHT_PURPLE)
                    .append(recipient.displayName())
                    .append(Component.text(": " + message, NamedTextColor.WHITE)));
            recipient.sendMessage(Component.text("From ", NamedTextColor.LIGHT_PURPLE)
                    .append(event.getPlayer().displayName())
                    .append(Component.text(": " + message, NamedTextColor.WHITE)));
        }
    }
}
