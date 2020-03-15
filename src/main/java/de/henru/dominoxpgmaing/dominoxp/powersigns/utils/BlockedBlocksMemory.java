package de.henru.dominoxpgmaing.dominoxp.powersigns.utils;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class holds a list of all blocks being blocked temporary for modification by powersign
 */
public class BlockedBlocksMemory {

    //Own Instance
    private static BlockedBlocksMemory instance;

    //List of all blocks being blocked
    private List<Location> blockedLocations;

    //Private Constructor
    private BlockedBlocksMemory(){
        blockedLocations = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Get the one instance of memory
     * @return Instance of Blocked blocks
     */
    public static BlockedBlocksMemory getInstance(){
        if(instance == null){
            instance = new BlockedBlocksMemory();
        }
        return instance;
    }

    /**
     * Add a new block / location to be blocked temporary
     * @param location the location to block
     */
    public void blockLocation(Location location){
        blockedLocations.add(location);
    }

    /**
     * Check if a given location is being blocked
     * @param location the location to check
     * @return true if block is blocked
     */
    public boolean isLocationBlocked(Location location){
        return blockedLocations.contains(location);
    }

    /**
     * Remove the given location from the blocked list
     * @param location location to unblock
     */
    public void unblockLocation(Location location){
        blockedLocations.remove(location);
    }
}
