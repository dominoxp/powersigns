package de.henru.dominoxpgmaing.dominoxp.powersigns.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class SignUtils {
    public static final int LINE_BRANDING = 0;
    public static final int LINE_OWNER = 1;
    public static final int LINE_DESCRIPTION = 2;
    public static final int LINE_MONEY = 3;


    private SignUtils(){}

    /**
     * Check if the given sign line matches the plugin keywords
     * @param line the line to check
     * @return true if the line matches a keyword defined in config.yml
     */
    public static boolean checkLineBranding(String line){
        //Normalize line
        line = normalizeLine(line);

        //TODO: Retreive Sign Headers from config files
        String[] sign_headers = new String[]{"[PowerSign]","[PowerSigns]","[PS]"};

        //Check if the first line equals the magic keyword (Default: [PowerSigns])
        for (String keyWord : sign_headers) {
            if(line.equalsIgnoreCase(keyWord)){
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the given line contains the same player name
     * @param line Sign line with player name
     * @param player the player creating the sign
     * @return true if name is matching
     */
    public static boolean checkSamePlayerName(String line, Player player){
        return player.getName().equalsIgnoreCase(normalizeLine(line));
    }


    /**
     * Check if the given player name has played before
     * @param line sign line to get name from
     * @return Offline Player if existing or null
     */
    @Nullable
    public static OfflinePlayer getPlayerBySignLine(String line){
        line = normalizeLine(line);

        //TODO: Find a better solution with player name caching!
        for(OfflinePlayer offlinePlayer: Bukkit.getOfflinePlayers()){
            if(
                    offlinePlayer.getName()!= null &&
                    offlinePlayer.getName().equalsIgnoreCase(line)
            ){
                return offlinePlayer;
            }
        }

        return null;
    }

    /**
     * Get the money amount of the sign created
     * @param line sign line
     * @return amount of money/int
     */
    public static int getMoneyAmount(String line){
        try{
            return Math.abs(Integer.parseInt(normalizeLine(line)));
        }catch (NumberFormatException e){
            return 0;
        }
    }

    /**
     * Check if given block is a sign
     * @param block Block to be tested
     * @return true if this is a sign
     */
    public static boolean isSignBlock(Block block){
        BlockState blockState = block.getState();
        return blockState instanceof org.bukkit.block.Sign;
    }

    /**
     * Normalize a sign line to be without color and leading spaces
     * @param line the Line
     * @return normalized line
     */
    private static String normalizeLine(String line){
        return ChatColor.stripColor(line).trim();
    }
}
