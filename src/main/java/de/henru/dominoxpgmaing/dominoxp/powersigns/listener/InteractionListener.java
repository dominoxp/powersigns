package de.henru.dominoxpgmaing.dominoxp.powersigns.listener;

import de.henru.dominoxpgmaing.dominoxp.powersigns.PowerSigns;
import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.BlockUtils;
import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.SignUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionListener implements Listener {

    /**
     * Handle a player block interaction event
     * @param event the event recurring
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        //Handle Right Click Action
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

            //Check if given block is a sign
            if(event.getClickedBlock() != null && SignUtils.isSignBlock(event.getClickedBlock())){
                //Get sign data
                Sign sign = (Sign) event.getClickedBlock().getState();
                String[] lines = sign.getLines();

                //Check if this is a powersign
                if(SignUtils.checkLineBranding(lines[SignUtils.LINE_BRANDING])){

                    //TODO: Check before transaction if(BlockUtils.canPowerBlock(sign.getLocation()))
                    //initiate transaction


                    int money = SignUtils.getMoneyAmount(lines[SignUtils.LINE_MONEY]);
                    Economy economy = PowerSigns.getEconomy();
                    //TODO: Check async
                    //TODO: Show confirmation on high amount
                    /*if(economy.has(event.getPlayer(), money)){


                    }*/

                    if(BlockUtils.canPowerBlock(sign.getLocation())){
                        BlockUtils.activateRedstoneSignal(sign.getLocation(),2);
                    }else{
                        //TODO:
                    }


                }
            }
        }


    }
}
