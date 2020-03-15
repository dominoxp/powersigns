/*******************************************************************************
 * Copyright (c) 2020 Jan (dominoxp@henru.de).
 * All rights reserved.
 ******************************************************************************/

package de.henru.dominoxpgmaing.dominoxp.powersigns.listener;

import de.henru.dominoxpgmaing.dominoxp.powersigns.PowerSigns;
import de.henru.dominoxpgmaing.dominoxp.powersigns.utils.BlockedBlocksMemory;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class BlockChangeListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        //Check if the block can be modified
        if (BlockedBlocksMemory.getInstance().isLocationBlocked(event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(PowerSigns.getSettings().getErrorCannotBreakWhileBlocked());
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            //Check if the block can be modified
            if (BlockedBlocksMemory.getInstance().isLocationBlocked(block.getLocation())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            //Check if the block can be modified
            if (BlockedBlocksMemory.getInstance().isLocationBlocked(block.getLocation())) {
                event.setCancelled(true);
                return;
            }
        }
    }


}
