package de.henru.dominoxpgmaing.dominoxp.powersigns.listener;

import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.InvalidPowerSignException;
import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.PowerSign;
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

        PowerSign powerSign;
        try {
            powerSign = new PowerSign(lines, event.getBlock().getLocation());
        }catch (InvalidPowerSignException e){
            //We don't care why the given block was not valid
            throw new NullPointerException(e.getMessage());
            //return;
        }

        //Check if we can place a redstone block on activation, if not cancel placement
        if(!powerSign.canPowerBlock()){
            player.sendMessage("Cannot Power this Block");//TODO: Add perm text
            event.setCancelled(true);
            return;
        }

        //Check if the sign contains the player name, if not get the corresponding player
        if(powerSign.isSamePlayerName(event.getPlayer())){
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
            //Update sign with current name if the given player could not be found
            powerSign.setPlayer(event.getPlayer());
        }

        //Check if the sign attached block can be powered
        if(!powerSign.canPowerBlock()){
            player.sendMessage("You cannot power this Block");//TODO: Add perm text
            event.setCancelled(true);
            return;
        }

        //Update the sign lines
        int index = 0;
        for(String line: powerSign.getFormattedLines()) {
            event.setLine(index, line);
            index += 1;
        }
    }
}
