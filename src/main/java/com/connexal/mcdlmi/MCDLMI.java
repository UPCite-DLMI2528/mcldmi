package com.connexal.mcdlmi;

import com.connexal.mcdlmi.api.component.ComponentManager;
import com.connexal.mcdlmi.api.config.ConfigurationManager;
import com.connexal.mcdlmi.core.component.DefaultComponentManager;
import com.connexal.mcdlmi.core.config.DefaultConfigurationManager;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class MCDLMI extends JavaPlugin {
    private static Logger logger;
    private static Server server;
    private static JavaPlugin plugin;

    private ComponentManager componentManager;

    @Override
    public void onEnable() {
        logger = this.getLogger();
        server = this.getServer();
        plugin = this;

        ConfigurationManager configManager = new DefaultConfigurationManager(this);
        this.componentManager = new DefaultComponentManager(this, configManager);
        this.componentManager.load();
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        this.componentManager.unload();
    }

    public static Logger getLog() {
        return logger;
    }

    public static void scheduleTask(Runnable runnable) {
        server.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void scheduleTask(Runnable runnable, int secondsDelay) {
        server.getScheduler().runTaskLaterAsynchronously(plugin, runnable, secondsDelay * 20L);
    }

    public static void scheduleRepeatingTask(Runnable runnable, int secondsInterval) {
        server.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0L, secondsInterval * 20L);
    }
}
