package com.connexal.mcdlmi.api.component;

import com.connexal.mcdlmi.api.config.Config;
import com.connexal.mcdlmi.api.config.ConfigCodec;
import com.connexal.mcdlmi.api.config.ConfigurationManager;
import com.connexal.mcdlmi.api.registry.RegistryHub;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Supplier;

public interface ComponentContext {
    ComponentInfo component();

    ComponentManager componentManager();

    RegistryHub registries();

    JavaPlugin plugin();

    Server server();

    Supplier<Config> configSupplier(ConfigCodec codec);

    default Supplier<Config> configSupplier() {
        return configSupplier(ConfigurationManager.DEFAULT_CONFIG_CODEC);
    }
}
