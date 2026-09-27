package com.connexal.mcdlmi.core.event;

import com.connexal.mcdlmi.api.event.EventRegistry;
import com.connexal.mcdlmi.core.registry.AbstractUnnamedRegistry;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class DefaultEventRegistry extends AbstractUnnamedRegistry<Listener> implements EventRegistry {
    private final Plugin plugin;

    public DefaultEventRegistry(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void afterRegister(Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    protected void afterUnregister(Listener listener) {
        HandlerList.unregisterAll(listener);
    }
}
