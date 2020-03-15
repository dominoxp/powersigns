/*******************************************************************************
 * Copyright (c) 2020 Jan (dominoxp@henru.de).
 * All rights reserved.
 ******************************************************************************/

package de.henru.dominoxpgmaing.dominoxp.powersigns;

import de.henru.dominoxpgmaing.dominoxp.powersigns.config_objects.ConfigAccess;
import de.henru.dominoxpgmaing.dominoxp.powersigns.config_objects.Settings;
import de.henru.dominoxpgmaing.dominoxp.powersigns.listener.BlockChangeListener;
import de.henru.dominoxpgmaing.dominoxp.powersigns.listener.ConfirmMoneyTransaction;
import de.henru.dominoxpgmaing.dominoxp.powersigns.listener.InteractionListener;
import de.henru.dominoxpgmaing.dominoxp.powersigns.listener.SignChangeListener;
import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.BlockedBlocksMemory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public final class PowerSigns extends JavaPlugin {
    private static final String LOG_TAG = PowerSigns.class.getSimpleName();
    private static final String DEPENDENCY_VAULT = "vault";
    private static final Logger log = Logger.getLogger("minecraft");

    private static PowerSigns instance;

    private static Economy economy = null;
    private static ConfigAccess configAccess;

    public static Economy getEconomy() {
        return economy;
    }

    public static PowerSigns getInstance() {
        return instance;
    }

    public static Settings getSettings() {
        return configAccess.getSettings();
    }

    /**
     * Callback when the plugin is enabled
     */
    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        log.info("[" + LOG_TAG + "] Enabling Plugin " + getDescription().getName() + " version " + getDescription().getVersion());

        if (setupEconomy()) {
            log.info("[" + LOG_TAG + "] Vault Economy Plugin Support enabled");
        } else {
            log.info("[" + LOG_TAG + "] Vault Economy Plugin Support not available because VAULT Plugin (https://www.spigotmc.org/resources/vault.34315/) or a supporting economy plugin is missing");
            onDisable();
        }

        try {
            configAccess = new ConfigAccess(this);
        } catch (IOException e) {
            log.warning("[" + LOG_TAG + "] The Configuration file could not be read, check if the server has read/write permissions to plugin config folder!");
            log.warning(e.toString());
            onDisable();
            return;
        }

        Objects.requireNonNull(getCommand("powersignacceptTransfer")).setExecutor(new ConfirmMoneyTransaction());
        registerListeners();

        //Initiate an instance of blocked blocks storage/memory
        BlockedBlocksMemory.getInstance();
    }

    /**
     * Callback when the plugin is disabled
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        BlockedBlocksMemory.getInstance().destroy();
        log.info("[" + LOG_TAG + "] Disabled Plugin " + getDescription().getName() + " Version " + getDescription().getVersion());
    }

    /**
     * Setup the connection to vault economy plugin
     *
     * @return true if the connection was successfully
     */
    private boolean setupEconomy() {
        //Check if vault is available
        if (getServer().getPluginManager().getPlugin(DEPENDENCY_VAULT) == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return true;
    }

    /**
     * Register all event listeners
     */
    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SignChangeListener(), this);
        pluginManager.registerEvents(new InteractionListener(), this);
        pluginManager.registerEvents(new BlockChangeListener(), this);
    }

}
