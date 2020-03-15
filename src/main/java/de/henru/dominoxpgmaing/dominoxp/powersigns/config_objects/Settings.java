package de.henru.dominoxpgmaing.dominoxp.powersigns.config_objects;

import dev.anhcraft.confighelper.ConfigSchema;
import dev.anhcraft.confighelper.annotation.*;

@Schema
public class Settings {
    public static final ConfigSchema<Settings> SCHEMA = ConfigSchema.of(Settings.class);

    //Permission releated messages

    @Key("msg.perm.create_sign.self")
    @Explanation("Error Message if the user has not the permissions to create a PowerSign for himself")
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String permErrorCreateSignSelf = "You don't have the permission to create a PowerSign";
    //General error messages
    @Key("msg.error.invalid_attached_block")
    @Explanation({
            "Error Message if the attached block cannot be replaced by a redstoneblock",
            "Use {block_name} to insert the attaching block name"})
    @Validation(notNull = true)
    @IgnoreValue(ifNull = true, ifEmptyString = true)
    private String errorInvalidAttachedBlock = "You cannot create a PowerSign on a {block_name}";

    public String getPermErrorCreateSignSelf() {
        return permErrorCreateSignSelf;
    }

    public String getErrorInvalidAttachedBlock() {
        return errorInvalidAttachedBlock;
    }
}
