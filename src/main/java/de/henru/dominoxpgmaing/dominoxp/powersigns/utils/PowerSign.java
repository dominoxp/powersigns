package de.henru.dominoxpgmaing.dominoxp.powersigns.utils;

import de.henru.dominoxpgmaing.dominoxp.powersigns.PowerSigns;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nullable;

public class PowerSign {
    public static final int LINE_BRANDING = 0;
    public static final int LINE_OWNER = 1;
    public static final int LINE_DESCRIPTION = 2;
    public static final int LINE_MONEY = 3;

    public static final String CURRENCY = "$";

    private String username;
    private String description;
    private float money;
    private Location location;

    public PowerSign(String[] lines, Location location) throws InvalidPowerSignException{
        //Check first line branding
        if(!checkBranding(
                normalizeLine(
                        lines[LINE_BRANDING]
                )
        )){
            throw new InvalidPowerSignException("The given sign header does not match [Powersign] or other specified in config");
        }

        username = normalizeLine(lines[LINE_OWNER]);

        description = lines[LINE_DESCRIPTION];

        money = getMoneyAmount(
                normalizeLine(lines[LINE_MONEY])
        );
        this.location = location;
    }

    public PowerSign(Block block) throws InvalidPowerSignException{
        this(getLinesFromBlock(block), block.getLocation());
    }

    /**
     * Get the text line of a minecraft sign, throw an exception if not a sign
     * @param block the block to get the text from
     * @return the lines on the sign
     * @throws InvalidPowerSignException if the given block is not a sign
     */
    private static String[] getLinesFromBlock(Block block) throws InvalidPowerSignException{
        BlockState blockState = block.getState();
        if(blockState instanceof org.bukkit.block.Sign){
            org.bukkit.block.Sign sign = (org.bukkit.block.Sign) blockState;
            return sign.getLines();
        }else{
            throw new InvalidPowerSignException("The given Block is not a Sign!");
        }
    }

    /**
     * Check if the given sign line matches the plugin keywords
     * @param line the line to check
     * @return true if the line matches a keyword defined in config.yml
     */
    private boolean checkBranding(String line){
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
     * Check if the given player name has played before
     * @return Offline Player if existing or null
     */
    @Nullable
    public OfflinePlayer getPlayer(){
        //TODO: Find a better solution with player name caching!
        for(OfflinePlayer offlinePlayer: Bukkit.getOfflinePlayers()){
            if(
                    offlinePlayer.getName()!= null &&
                            offlinePlayer.getName().equalsIgnoreCase(username)
            ){
                return offlinePlayer;
            }
        }

        return null;
    }

    /**
     * Set the given player name as the owner player name
     * @param player the player to be set
     */
    public void setPlayer(OfflinePlayer player){
        username = player.getName();
    }

    /**
     * Check if the given line contains the same player name
     * @param player the player creating the sign
     * @return true if name is matching
     */
    public boolean isSamePlayerName(Player player){
        return player.getName().equalsIgnoreCase(getUsername());
    }

    /**
     * Get the money amount of the sign created
     * @param line sign line
     * @return amount of money/int
     */
    private float getMoneyAmount(String line){
        //Remove currency symbol
        line = line.replace(CURRENCY, "");

        //Try to parse a float first
        float amount;
        try{
            amount = Float.parseFloat(line);
        }catch (NumberFormatException e){
            //Try to parse a integer
            try {
                amount = Integer.parseInt(line);
            }catch (NumberFormatException ee){
                amount = 0;
            }
        }

        //Make Sure Amount is positiv
        return Math.abs(amount);
    }

    /**
     * Normalize a sign line to be without color and leading spaces
     * @param line the Line
     * @return normalized line
     */
    private static String normalizeLine(String line){
        return ChatColor.stripColor(line).trim();
    }

    /**
     * Create a redstone signal at the position where the sign is attached at for given delay in seconds
     */
    public void activateRedstoneSignal(){
        int delay = 1;
        Block attachedBlock = getAttachedBlock();

        //Check if attached block exists and the block was not locked by another click
        //Checking the sign would not prevent other signs to be clicked at same location
        if(attachedBlock == null){
            return;
        }

        //Check if given block can be replaced with redstone block
        if(!canPowerBlock()){
            return;
        }

        //Save the material and block data
        Material material = attachedBlock.getType();
        BlockData data = attachedBlock.getBlockData();

        int ticks = delay * 20;

        //Block block location
        BlockedBlocksMemory.getInstance().blockLocation(attachedBlock.getLocation());
        //Replace actual block
        attachedBlock.setType(Material.REDSTONE_BLOCK);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PowerSigns.getInstance(), () -> {
            attachedBlock.setType(material);
            attachedBlock.setBlockData(data);
            BlockedBlocksMemory.getInstance().unblockLocation(attachedBlock.getLocation());
        }, ticks);

        return;
    }

    /**
     * Gets the block the sign is standing on or attached to
     * @return The block the sign is standing on or attached to
     */
    private Block getAttachedBlock() {
        if(location.getWorld() != null) {
            Block block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            BlockData data = block.getBlockData();

            //Check if sign is mounted on wall, else use Block below
            if(data instanceof Directional){
                Directional directional = (Directional) data;
                return block.getRelative(directional.getFacing().getOppositeFace());
            }else{
                return block.getRelative(BlockFace.DOWN);
            }
        }
        return null;
    }

    /**
     * Check if the attached block can safely replaced with a redstone block
     * @return true if block can be replaced
     */
    public boolean canPowerBlock(){
        //Check if the block is currently powered by redstone
        if(BlockedBlocksMemory.getInstance().isLocationBlocked(getLocation())){
            return false;
        }

        Block attachedBlock = getAttachedBlock();
        //TODO: Check if the block has extra meta data like signs, will erase signs otherwise
        return !(attachedBlock == null || attachedBlock.getState() instanceof InventoryHolder);
    }

    public String getUsername(){
        return normalizeLine(username);
    }

    public String getDescription() {
        return description;
    }

    public float getMoney() {
        return money;
    }

    public Location getLocation() {
        return location;
    }

    public String[] getFormattedLines(){
        String[] lines = new String[4];

        //Format the new Sign
        lines[LINE_BRANDING] = "§4[PowerSign]";//TODO: Read from config
        lines[LINE_OWNER] = username;
        lines[LINE_DESCRIPTION] = description;
        lines[LINE_MONEY] = String.format("%s%s", money, CURRENCY);

        return lines;
    }
}
