package de.henru.dominoxpgmaing.dominoxp.powersigns;

import de.henru.dominoxpgmaing.dominoxp.powersigns.listener.BlockChangeListener;
import de.henru.dominoxpgmaing.dominoxp.powersigns.listener.ConfirmMoneyTransaction;
import de.henru.dominoxpgmaing.dominoxp.powersigns.listener.InteractionListener;
import de.henru.dominoxpgmaing.dominoxp.powersigns.listener.SignChangeListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class PowerSigns extends JavaPlugin {
    private static final String LOG_TAG = PowerSigns.class.getSimpleName();
    private static final String DEPENDENCY_VAULT = "vault";
    private static final Logger log = Logger.getLogger("minecraft");

    private static PowerSigns instance;

    private static Economy economy = null;
    private static Permission permissions;
    private static Chat chat;

    /**
     * Callback when the plugin is enabled
     */
    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        log.info("[" + LOG_TAG + "] Enabling Plugin " + getDescription().getName() + " version " + getDescription().getVersion());

        if(setupEconomy()){
            log.info("[" + LOG_TAG + "] Vault Economy Plugin Support enabled");
        }else{
            log.info("[" + LOG_TAG + "] Vault Economy Plugin Support not available because VAULT Plugin (https://www.spigotmc.org/resources/vault.34315/) or a supporting economy plugin is missing");
            onDisable();
        }

        setupPermissions();
        getCommand("powersignacceptTransfer").setExecutor(new ConfirmMoneyTransaction());
        registerListeners();

    }

    /**
     * Callback when the plugin is disabled
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log.info("[" + LOG_TAG + "] Disabled Plugin " + getDescription().getName() + " Version " + getDescription().getVersion());
    }

    /**
     * Setup the connection to vault economy plugin
     * @return true if the connection was successfully
     */
    private boolean setupEconomy(){
        //Check if vault is available
        if (getServer().getPluginManager().getPlugin(DEPENDENCY_VAULT) == null){
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null){
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }

    /**
     * Setup Permissions Support
     * @return true if setup was successfully
     */
    private boolean setupPermissions(){
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);

        if(rsp == null){
            return false;
        }

        permissions = rsp.getProvider();
        return permissions != null;
    }

    /**
     * Register all event listeners
     */
    private void registerListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SignChangeListener(), this);
        pluginManager.registerEvents(new InteractionListener(), this);
        pluginManager.registerEvents(new BlockChangeListener(), this);
    }

    public static Economy getEconomy(){
        return economy;
    }

    public static Permission getPermissions(){
        return permissions;
    }

    public static PowerSigns getInstance(){
        return instance;
    }

}
