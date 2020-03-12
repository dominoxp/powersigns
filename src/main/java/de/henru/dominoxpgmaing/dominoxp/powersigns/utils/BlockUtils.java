package de.henru.dominoxpgmaing.dominoxp.powersigns.utils;

import de.henru.dominoxpgmaing.dominoxp.powersigns.PowerSigns;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitScheduler;


public class BlockUtils {
    public static void activateRedstoneSignal(Location location, int delay){
        Block attachedBlock = getSignBlock(location);

        assert attachedBlock != null;
        Material material = attachedBlock.getType();
        BlockData data = attachedBlock.getBlockData();
        int ticks = delay * 20;

        attachedBlock.setType(Material.REDSTONE_BLOCK);

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        scheduler.scheduleSyncDelayedTask(PowerSigns.getInstance(), () -> attachedBlock.setType(material), ticks);
        scheduler.scheduleSyncDelayedTask(PowerSigns.getInstance(), () -> attachedBlock.setBlockData(data), ticks);
    }

    /**
     * Gets the block the sign is standing on or attached to
     * @return The block the sign is standing on or attached to
     */
    public static Block getSignBlock(Location location) {
        if(location.getWorld() != null) {
            Block block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            BlockData data = block.getBlockData();

            //Check if sign is mounted on wall, else use Block below
            if(data instanceof  Directional){
                Directional directional = (Directional) data;
                return block.getRelative(directional.getFacing().getOppositeFace());
            }else{
                return block.getRelative(BlockFace.DOWN);
            }
        }
        return null;
    }

    public static boolean canPowerBlock(Location location){
        Block attachedBlock = getSignBlock(location);
        return !(attachedBlock == null || attachedBlock.getState() instanceof InventoryHolder);
    }

}
