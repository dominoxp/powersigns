/*******************************************************************************
 * Copyright (c) 2020 Jan (dominoxp@henru.de).
 * All rights reserved.
 ******************************************************************************/

package de.henru.dominoxpgmaing.dominoxp.powersigns.listener;

import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.MoneyUtils;
import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.PowerSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConfirmMoneyTransaction implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 4) {
                sender.sendMessage("Too few arguments provided");
            }

            Location location;
            try {
                location = new Location(
                        Bukkit.getWorld(args[0]),
                        Float.parseFloat(args[1]),
                        Float.parseFloat(args[2]),
                        Float.parseFloat(args[3])
                );

                PowerSign powerSign = new PowerSign(location.getBlock());
                MoneyUtils.startMoneyTransaction(player, powerSign.getPlayer(), powerSign.getMoney(), powerSign, false);
                return true;

            } catch (Exception e) {
                sender.sendMessage("Invalid Coordinates: " + e);
            }
        }
        return false;
    }
}
