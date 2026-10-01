package com.connexal.mcdlmi.components.totalitarian1984.listeners;

import com.connexal.mcdlmi.components.totalitarian1984.Abstract1984Listener;
import com.connexal.mcdlmi.components.totalitarian1984.Logging1984;
import com.connexal.mcdlmi.components.totalitarian1984.utils.StringConversion;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.*;

public class ChatListener extends Abstract1984Listener {
    public ChatListener(Logging1984 logger) {
        super("chat", logger);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerChat(AsyncChatEvent event) {
        this.log(StringConversion.fromComponent(event.message()), event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        this.log(event.getMessage(), event.getPlayer());
    }
}
