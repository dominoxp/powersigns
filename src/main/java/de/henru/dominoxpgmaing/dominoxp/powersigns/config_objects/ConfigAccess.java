/*
 * Copyright (c) 2020 Jan (dominoxp@henru.de).
 * All rights reserved.
 */

package de.henru.dominoxpgmaing.dominoxp.powersigns.config_objects;

import dev.anhcraft.configdoc.ConfigDocGenerator;
import dev.anhcraft.confighelper.ConfigHelper;
import dev.anhcraft.confighelper.exception.InvalidValueException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ConfigAccess {
    private static final String CONFIG_POWERSIGNS_PATH = "powersigns.yml";
    private static final String CONFIG_POWERSIGNS_DOCS_PATH = "docs";

    private File configFile;
    private Settings settings;

    /**
     * Initiate a config instance
     *
     * @param plugin the plugin accessing the configs
     * @throws IOException if the config file cannot be read or written
     */
    public ConfigAccess(Plugin plugin) throws IOException {
        //Check if default setting is present
        configFile = new File(plugin.getDataFolder(), CONFIG_POWERSIGNS_PATH);

        //Create parent folders
        if (!configFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            configFile.getParentFile().mkdirs();

            //Save default config
            settings = new Settings();
            saveConfig();

            //Generate settings docs
            new ConfigDocGenerator().withSchema(Settings.SCHEMA).generate(new File(plugin.getDataFolder(), CONFIG_POWERSIGNS_DOCS_PATH));
        }

        //Try to load settings
        try {
            loadConfig();
        } catch (InvalidValueException | InvalidConfigurationException e) {
            //On Error initialise a new instance of the settings
            Logger.getLogger("minecraft").warning(
                    String.format("The Configuration File '%s' was invalid because of error: %s", configFile, e));
            Logger.getLogger("minecraft").warning("Using the default config instead!");
            settings = new Settings();
        }
    }

    /**
     * load the plugin configuration
     *
     * @throws InvalidValueException         a problem with the configuration file
     * @throws IOException                   File access error
     * @throws InvalidConfigurationException a problem with the configuration file
     */
    public void loadConfig() throws InvalidValueException, IOException, InvalidConfigurationException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.load(configFile);
        settings = ConfigHelper.readConfig(configuration, Settings.SCHEMA);
    }

    /**
     * Save the current configuration
     *
     * @throws IOException the file could not be written
     */
    public void saveConfig() throws IOException {
        YamlConfiguration configuration1 = new YamlConfiguration();
        ConfigHelper.writeConfig(configuration1, Settings.SCHEMA, settings, ConfigHelper.newOptions().ignoreFalse().ignoreZero());
        configuration1.save(configFile);
    }

    public Settings getSettings() {
        return settings;
    }
}
