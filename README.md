# Minecraft PowerSigns by dominoxp
A Spigot Plugin to create Powersigns activating a Redstone Signal if Rightclicked, with additionaly money transaction for Spigot Minecraft 1.15+
https://github.com/dominoxp/powersigns

## Features
You can create a PowerSign, a regular Sign with [PowerSign] in the first line. The PowerSign can be activated by right clicking and will convert the block the sign is attached to to a redstone block for 1second. The activation can additionally require an amount of money to be transfered to other players.

- Create a PowerSign
- Signs can activate Redstone Signals on rigth click
- Action can cost a confirured amount of money
- At given money limit the player hast to confirm right click
- Blocks like Chests or Signs cannot be converted to Redstone Blocks to prevent item loss
- Active PowerSigns cannot be broken or moved by Pistons to prevent Redstone Farming

## Requirement
- Requires Minecraft Version 1.15+
- Requires [Vault](https://www.spigotmc.org/resources/vault.34315/) Plugin
- Requires a Economy Plugin that support Vault, otherwise it can't access the vault api

## Permissions
- To Activate a PowerSign:
	- powersigns.sign.use
- To Create a PowerSign for yourself:
	- powersigns.sign.create.self
- To Create a PowerSign for other Players (receiving the money):
	- powersigns.sign.create.other

## Config

| Key                                    | Type                  | Explanation                                                                                                                                                                                                                                                                        |
|----------------------------------------|-----------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| config.plugin_prefix                   | String not-null       | The name and appearance of the plugin messages in chat                                                                                                                                                                                                                             |
| config.confirm_if_more_than            | int not-null          | The user should confirm the transaction while clicking on the sign at given money amount (int)                                                                                                                                                                                     |
| config.powersign.valid_header          | List<String> not-null | The Sign Headers to recognise a powersign usually [ps] (not case sensitive) Please make sure that config.powersign.replace_header is present here without color Otherwise the placed signs could not be recognised                                                                 |
| config.powersign.replaced_header       | String not-null       | The Header used if the sign was recognized, you can use color codes here                                                                                                                                                                                                           |
| config.powersign.currency              | String not-null       | The Currency Symbol used to visualise money amount on sign, could be blank                                                                                                                                                                                                         |
| msg.perm.create_sign.self              | String not-null       | Error Message if the user has not the permissions to create a PowerSign for himself                                                                                                                                                                                                |
| msg.perm.create_sign.other             | String not-null       | Error Message if the user has not the permissions to create a PowerSign for other user Use {player_name} to insert the targed player name                                                                                                                                          |
| msg.error.invalid_player_name          | String not-null       | Error Message if the user name on the powersign could not be found Use {player_name} to insert the missing player name                                                                                                                                                             |
| msg.error.break_block_while_active     | String not-null       | Error Message if the user tries to break an active power sign                                                                                                                                                                                                                      |
| msg.error.transaction_not_enough_money | String not-null       | Error Message if the user name has not (config.confirm_if_more_than) Use {missing_money} to insert the missing money amount                                                                                                                                                        |
| msg.info.confirm_transfer              | String not-null       | Inform the player if the confirm money amount was reached The user should then confirm by clicking on the (msg.info.confirm_transfer_click) message Use {money_amount} to insert the money to pay to the player Use {destination_player} to insert the money receiving player name |
| msg.info.confirm_transfer_click        | String not-null       | The user should confirm the transaction by clicking on this text                                                                                                                                                                                                                   |
| msg.error.invalid_attached_block       | String not-null       | Error Message if the attached block cannot be replaced by a redstoneblock Use {block_name} to insert the attaching block name                                                                                                                                                      |

## Contact
[dominoxp](https://github.com/dominoxp)
