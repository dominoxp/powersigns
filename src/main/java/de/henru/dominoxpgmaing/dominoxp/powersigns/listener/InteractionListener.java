package de.henru.dominoxpgmaing.dominoxp.powersigns.listener;

import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.InvalidPowerSignException;
import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.MoneyUtils;
import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.PowerSign;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionListener implements Listener {

    /**
     * Handle a player block interaction event
     *
     * @param event the event recurring
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        //Handle Right Click Action
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            //Check if given block is a sign
            final PowerSign powerSign;

            try {
                powerSign = new PowerSign(event.getClickedBlock());
            } catch (InvalidPowerSignException e) {
                //The given block was not a power sign, ignore it
                return;
            }

            //Check if we can activate a redstone signal
            if (powerSign.canPowerBlock()) {

                //Check if this is the same player, active redstone and don't charge money
                if (powerSign.isSamePlayerName(event.getPlayer())) {
                    powerSign.activateRedstoneSignal();
                } else {
                    OfflinePlayer moneyDestination = powerSign.getPlayer();
                    if (moneyDestination == null) {
                        event.getPlayer().sendMessage(String.format("The Destination User %s could not be found!", powerSign.getUsername()));//TODO: Add to config
                        return;
                    }
                    float money = powerSign.getMoney();

                    MoneyUtils.startMoneyTransaction(event.getPlayer(), moneyDestination, money, powerSign);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }
}
