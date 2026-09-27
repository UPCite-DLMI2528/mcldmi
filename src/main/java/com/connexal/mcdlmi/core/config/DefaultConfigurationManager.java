package com.connexal.mcdlmi.core.config;

import com.connexal.mcdlmi.api.config.Config;
import com.connexal.mcdlmi.api.config.ConfigCodec;
import com.connexal.mcdlmi.api.config.ConfigurationManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DefaultConfigurationManager implements ConfigurationManager {
    private final JavaPlugin plugin;
    private final Map<String, Config> configs = new HashMap<>();

    public DefaultConfigurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private Config loadConfig(String configName, ConfigCodec configType) {
        configName += "." + configType.extension();
        File file = new File(this.plugin.getDataFolder(), configName);

        // Attempt to save the config file from the plugin's resources if it doesn't already exist
        if (!file.exists()) {
            try {
                this.plugin.saveResource(configName, false);
            } catch (IllegalArgumentException ignore) {
                // The resource doesn't exist in the plugin's jar, so we ignore this exception
            }
        }

        // Load the configuration file & save it
        Config config;
        switch (configType) {
            case YAML:
                config = new YamlConfig(file);
                break;
            default:
                throw new IllegalArgumentException("Unsupported config type: " + configType);
        }

        config.save();

        return config;
    }

    @Override
    public Config getConfig(String configName, ConfigCodec configType) {
        // Check if the config is already loaded and cached
        if (configs.containsKey(configName)) {
            return configs.get(configName);
        }

        // Load the config and cache it
        Config config = loadConfig(configName, configType);
        configs.put(configName, config);

        return config;
    }
}
