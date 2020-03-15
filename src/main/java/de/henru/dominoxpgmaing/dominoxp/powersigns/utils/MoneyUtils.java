/*
 * Copyright (c) 2020 Jan (dominoxp@henru.de).
 * All rights reserved.
 */

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

    /**
     * Start a Money Transaction from source to destination player with money amount
     * If the user should confirm the transaction (above limit) it is prompted to click on a text message
     * Otherwise the money will be transferred to the other player and active the redstone signal
     *
     * @param source        The player paying money
     * @param destination   The player receiving money
     * @param amount        the mount of money
     * @param powerSign     the sign being activated
     * @param promptConfirm should the user confirm the message, if false the transaction is considered confirmed
     */
    public static void startMoneyTransaction(Player source, OfflinePlayer destination, float amount, PowerSign powerSign, boolean promptConfirm) {
        Runnable task = () -> {
            if (transferMoney(source, destination, amount)) {
                //Activate the Redstone signal
                Bukkit.getServer().getScheduler().runTask(PowerSigns.getInstance(), powerSign::activateRedstoneSignal);
            } else {
                //Inform user of missing money
                Economy economy = PowerSigns.getEconomy();
                source.sendMessage(PowerSigns.getSettings().getErrorNotEnoughMoney((float) (amount - economy.getBalance(source))));
            }
        };

        //Check if the user should confirm the transaction
        if (promptConfirm && shouldUserConfirm(amount)) {
            source.sendMessage(
                    new ComponentBuilder(PowerSigns.getSettings().getMessageConfirmTransfer(amount, destination.getName()))
                            .append(PowerSigns.getSettings().getMessageConfirmTransferClickText())
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(
                                    "/powersignacceptTransfer %s %s %s %s",
                                    powerSign.getLocation().getWorld().getName(),
                                    powerSign.getLocation().getX(),
                                    powerSign.getLocation().getY(),
                                    powerSign.getLocation().getZ()
                            )))
                            .create());

        } else {
            //run the power sign task async to query money async
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
        return amount > PowerSigns.getSettings().getConfigMoneyAmountConfirm();
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
