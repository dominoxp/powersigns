package de.henru.dominoxpgmaing.dominoxp.powersigns.utils;

import de.henru.dominoxpgmaing.dominoxp.powersigns.PowerSigns;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class MoneyUtils {

    private MoneyUtils() {
    }

    public static void startMoneyTransaction(Player source, OfflinePlayer destination, float amount, PowerSign powerSign) {
        startMoneyTransaction(source, destination, amount, powerSign, true);
    }

    public static void startMoneyTransaction(Player source, OfflinePlayer destination, float amount, PowerSign powerSign, boolean promtConfirm) {
        Runnable task = () -> {
            if (transferMoney(source, destination, amount)) {
                Bukkit.getServer().getScheduler().runTask(PowerSigns.getInstance(), powerSign::activateRedstoneSignal);
            } else {
                source.sendMessage("Du hast nicht genug geld");
            }
        };

        //Check if the user should confirm the transaction
        if (promtConfirm && shouldUserConfirm(amount)) {
            source.sendMessage(
                    new ComponentBuilder("")
                            .color(net.md_5.bungee.api.ChatColor.GREEN)
                            .append("[PowerSigns]")
                            .color(net.md_5.bungee.api.ChatColor.RESET)
                            .append(String.format("Bestätige den Geldtransfer von %s%s zu %s ", amount, PowerSign.CURRENCY, destination.getName()))
                            .append("[Hier Klicken]")
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(
                                    "/powersignacceptTransfer %s %s %s %s",
                                    powerSign.getLocation().getWorld().getName(),
                                    powerSign.getLocation().getX(),
                                    powerSign.getLocation().getY(),
                                    powerSign.getLocation().getZ()
                            )))
                            .create());

        } else {
            Bukkit.getServer().getScheduler().runTaskAsynchronously(PowerSigns.getInstance(), task);
        }
    }

    /**
     * Check if we should show a confirmation dialog
     *
     * @param amount the mount to withdraw
     * @return true if a confirmation is required
     */
    public static boolean shouldUserConfirm(float amount) {
        return amount > 100;//TODO: use config
    }

    /**
     * Transfer money from source to destination player
     *
     * @param source      player sending money
     * @param destination player receiving money
     * @param amount      money amount
     * @return true is successful
     */
    private static boolean transferMoney(OfflinePlayer source, OfflinePlayer destination, float amount) {
        Economy economy = PowerSigns.getEconomy();
        if (economy.has(source, amount)) {
            economy.withdrawPlayer(source, amount);
            economy.depositPlayer(destination, amount);
            return true;
        }
        return false;
    }

}
