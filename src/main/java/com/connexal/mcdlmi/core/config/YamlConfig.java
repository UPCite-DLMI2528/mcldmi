package com.connexal.mcdlmi.core.config;

import com.connexal.mcdlmi.MCDLMI;
import com.connexal.mcdlmi.api.config.Config;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

public class YamlConfig extends YamlConfiguration implements Config {
    private final File file;

    public YamlConfig(File file) {
        try {
            this.load(file);
        } catch (FileNotFoundException ignore) {
        } catch (IOException ex) {
            MCDLMI.getLog().log(Level.SEVERE, "Cannot load config file " + file, ex);
        } catch (InvalidConfigurationException ex) {
            MCDLMI.getLog().log(Level.SEVERE, "Cannot load config file " + file + " due to invalid YAML configuration", ex);
        }

        this.file = file;
    }

    @Override
    public void save() {
        try {
            this.save(this.file);
        } catch (IOException ex) {
            MCDLMI.getLog().log(Level.SEVERE, "Cannot save config file " + this.file, ex);
        }
    }
}
