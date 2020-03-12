package de.henru.dominoxpgmaing.dominoxp.powersigns.listener;

import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.BlockUtils;
import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.SignUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
    private static final String PERMISSION_SIGN_CREATE_SELF = "powersigns.sign.create.self";
    private static final String PERMISSION_SIGN_CREATE_OTHER = "powersigns.sign.create.other";

    /**
     * On sign change listener
     * We are checking if the sign matches the following format:
     * [KEYWORD]
     * <Username>
     * Description
     * <Price>
     * @param event the event triggering this listener
     */
    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event){
        Player player = event.getPlayer();
        String[] lines = event.getLines();

        //The player receiving the money
        OfflinePlayer targetedPlayer = null;
        //The money amount to withdraw
        int money;


        if(!SignUtils.checkLineBranding(lines[SignUtils.LINE_BRANDING])){
            return;
        }

        //Check if we can place a redstone block on activation, if not cancel placement
        if(!BlockUtils.canPowerBlock(event.getBlock().getLocation())){
            //Inform layer about missing permissions
            player.sendMessage("Cannot Power this Block");//TODO: Add perm text
            event.setCancelled(true);
            return;
        }

        //Check if line 2 contains the player name, if not get the corresponding player
        if(!SignUtils.checkSamePlayerName(lines[SignUtils.LINE_OWNER], player)){
            targetedPlayer = SignUtils.getPlayerBySignLine(lines[SignUtils.LINE_OWNER]);
        }

        //Check if this is the player creating the sign
        if(targetedPlayer == null || player.getUniqueId() == targetedPlayer.getUniqueId()) {
            targetedPlayer = player;
            //Check permissions

            if(!player.hasPermission(PERMISSION_SIGN_CREATE_SELF)){
                //Inform layer about missing permissions
                player.sendMessage("Missing Permissions");//TODO: Add perm text
                event.setCancelled(true);
                return;
            }
        }else{
            if(!player.hasPermission(PERMISSION_SIGN_CREATE_OTHER)){
                //Inform layer about missing permissions
                player.sendMessage("Missing Permissions");//TODO: Add perm text
                event.setCancelled(true);
                return;
            }
        }

        //Get the money amount to withdraw
        money = SignUtils.getMoneyAmount(lines[SignUtils.LINE_MONEY]);

        //Format the new Sign
        event.setLine(SignUtils.LINE_BRANDING, "§4[PowerSign]");//TODO: Read from config
        event.setLine(SignUtils.LINE_OWNER, targetedPlayer.getName());
        //Line 2 will be ignored as there is only a description
        event.setLine(SignUtils.LINE_MONEY, Integer.toString(money));
    }
}
