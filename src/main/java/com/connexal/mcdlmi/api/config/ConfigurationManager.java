package com.connexal.mcdlmi.api.config;

public interface ConfigurationManager {
    String DEFAULT_CONFIG_NAME = "config";
    ConfigCodec DEFAULT_CONFIG_CODEC = ConfigCodec.YAML;

    /**
     * Loads and returns a configuration file of the specified type by name.
     * @param configName the name of the configuration file (without extension)
     * @param configType the type of the configuration file (e.g., YAML)
     * @return the loaded configuration file
     */
    Config getConfig(String configName, ConfigCodec configType);

    /**
     * Loads and returns a configuration file of the specified type by name.
     * @param configName the name of the configuration file (without extension)
     * @return the loaded configuration file
     */
    default Config getConfig(String configName) {
        return getConfig(configName, DEFAULT_CONFIG_CODEC);
    }

    /**
     * Loads and returns the default configuration file.
     * @return the loaded default configuration file
     */
    default Config getDefaultConfig() {
        return getConfig(DEFAULT_CONFIG_NAME, DEFAULT_CONFIG_CODEC);
    }
}
