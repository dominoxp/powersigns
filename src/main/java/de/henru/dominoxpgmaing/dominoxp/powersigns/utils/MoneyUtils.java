package de.henru.dominoxpgmaing.dominoxp.powersigns.utils;

import de.henru.dominoxpgmaing.dominoxp.powersigns.PowerSigns;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;

public class MoneyUtils {

    private MoneyUtils(){}

    /**
     * Check if we should show a confirmation dialog
     * @param amount the mount to withdraw
     * @return true if a confirmation is required
     */
    public static boolean shouldUserConfirm(int amount){
        return amount > 100;//TODO: use config
    }

    /**
     * Transfer money from source to destination player
     * @param source player sending money
     * @param destination player receiving money
     * @param amount money amount
     * @return true is successful
     */
    public static boolean transferMoney(OfflinePlayer source, OfflinePlayer destination, int amount){
        Economy economy = PowerSigns.getEconomy();
        if(economy.has(source, amount)){
            economy.withdrawPlayer(source, amount);
            economy.depositPlayer(destination, amount);
            return true;
        }
        return false;
    }

}
