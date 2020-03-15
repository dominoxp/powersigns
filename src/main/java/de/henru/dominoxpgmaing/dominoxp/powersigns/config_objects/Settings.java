/*******************************************************************************
 * Copyright (c) 2020 Jan (dominoxp@henru.de).
 * All rights reserved.
 ******************************************************************************/

package de.henru.dominoxpgmaing.dominoxp.powersigns.config_objects;

import dev.anhcraft.confighelper.ConfigSchema;
import dev.anhcraft.confighelper.annotation.*;
import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;
import java.util.List;

@Schema
public class Settings {
    public static final ConfigSchema<Settings> SCHEMA = ConfigSchema.of(Settings.class);
    private static final char COLOR_CODE = '&';

    //General Settings
    @Key("config.plugin_prefix")
    @Explanation({
            "The name and appearance of the plugin messages in chat"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String pluginChatPrefix = "&b[PowerSigns]&r";
    @Key("config.confirm_if_more_than")
    @Explanation({
            "The user should confirm the transaction while clicking on the sign at given money amount (int)"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private int configMoneyAmountConfirm = 10;
    @Key("config.powersign.valid_header")
    @Explanation({
            "The Sign Headers to recognise a powersign usually [ps] (not case sensitive)",
            "Please make sure that config.powersign.replace_header is present here without color",
            "Otherwise the placed signs could not be recognised"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private List<String> configValidSignHeaders = Arrays.asList("[ps]", "[powersign]", "[powersigns]");
    @Key("config.powersign.replaced_header")
    @Explanation({
            "The Header used if the sign was recognized, you can use color codes here"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String configSignHeaderResult = "&4[PowerSigns] ";
    @Key("config.powersign.currency")
    @Explanation({
            "The Currency Symbol used to visualise money amount on sign, could be blank"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true)
    private String configCurrencySymbol = "$";
    //Permission related messages
    @Key("msg.perm.create_sign.self")
    @Explanation({
            "Error Message if the user has not the permissions to create a PowerSign for himself"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String permErrorCreateSignSelf = "You don't have the permission to create a PowerSign";
    @Key("msg.perm.create_sign.other")
    @Explanation({
            "Error Message if the user has not the permissions to create a PowerSign for other user",
            "Use {player_name} to insert the targed player name"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String permErrorCreateSignOther = "You don't have the permission to create a PowerSign for {player_name}";
    @Key("msg.error.invalid_player_name")
    @Explanation({
            "Error Message if the user name on the powersign could not be found",
            "Use {player_name} to insert the missing player name"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String errorDestinationUserNotFound = "The Destination User {player_name} could not be found!";
    @Key("msg.error.break_block_while_active")
    @Explanation({
            "Error Message if the user tries to break an active power sign"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String errorCannotBreakWhileBlocked = "This block cannot be broken while the powersign is active!";
    @Key("msg.error.transaction_not_enough_money")
    @Explanation({
            "Error Message if the user name has not (config.confirm_if_more_than)",
            "Use {missing_money} to insert the missing money amount"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String errorNotEnoughMoney = "You do not have enough money to activate this sign, you are missing {missing_money}";
    @Key("msg.info.confirm_transfer")
    @Explanation({
            "Inform the player if the confirm money amount was reached",
            "The user should then confirm by clicking on the (msg.info.confirm_transfer_click) message",
            "Use {money_amount} to insert the money to pay to the player",
            "Use {destination_player} to insert the money receiving player name"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String messageConfirmTransfer = "Please confirm the money transfer of {money_amount} to {destination_player} ";
    @Key("msg.info.confirm_transfer_click")
    @Explanation({
            "The user should confirm the transaction by clicking on this text"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String messageConfirmTransferClickText = "&a[Click here]";

    public String getPluginChatPrefix() {
        return ChatColor.translateAlternateColorCodes(COLOR_CODE, pluginChatPrefix);
    }

    public int getConfigMoneyAmountConfirm() {
        return configMoneyAmountConfirm;
    }


    //General error messages
    @Key("msg.error.invalid_attached_block")
    @Explanation({
            "Error Message if the attached block cannot be replaced by a redstoneblock",
            "Use {block_name} to insert the attaching block name"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String errorInvalidAttachedBlock = "You cannot create a PowerSign on a {block_name}";

    public List<String> getConfigValidSignHeaders() {
        return configValidSignHeaders;
    }

    public String getConfigSignHeaderResult() {
        return ChatColor.translateAlternateColorCodes(COLOR_CODE, configSignHeaderResult);
    }

    public String getConfigCurrencySymbol() {
        return configCurrencySymbol;
    }

    public String getPermErrorCreateSignSelf() {
        return ChatColor.translateAlternateColorCodes(COLOR_CODE, pluginChatPrefix + permErrorCreateSignSelf);
    }

    public String getPermErrorCreateSignOther(String playerName) {
        return ChatColor.translateAlternateColorCodes(COLOR_CODE, pluginChatPrefix + permErrorCreateSignOther.replace("{player_name}", playerName));
    }

    public String getErrorInvalidAttachedBlock(String blockName) {
        return ChatColor.translateAlternateColorCodes(COLOR_CODE, pluginChatPrefix + errorInvalidAttachedBlock.replace("{block_name}", blockName));
    }

    public String getErrorDestinationUserNotFound(String playerName) {
        return ChatColor.translateAlternateColorCodes(COLOR_CODE, pluginChatPrefix + errorDestinationUserNotFound.replace("{player_name}", playerName));
    }

    public String getErrorCannotBreakWhileBlocked() {
        return ChatColor.translateAlternateColorCodes(COLOR_CODE, pluginChatPrefix + errorCannotBreakWhileBlocked);
    }

    public String getErrorNotEnoughMoney(float missingMoney) {
        return ChatColor.translateAlternateColorCodes(COLOR_CODE, pluginChatPrefix + errorNotEnoughMoney.replace("{missing_money}", Float.toString(missingMoney)));
    }

    public String getMessageConfirmTransfer(float money, String destinationPlayer) {
        return ChatColor.translateAlternateColorCodes(COLOR_CODE, pluginChatPrefix + messageConfirmTransfer
                .replace("{money_amount}", Float.toString(money))
                .replace("{destination_player}", destinationPlayer));
    }

    public String getMessageConfirmTransferClickText() {
        return ChatColor.translateAlternateColorCodes(COLOR_CODE, messageConfirmTransferClickText);
    }
}
