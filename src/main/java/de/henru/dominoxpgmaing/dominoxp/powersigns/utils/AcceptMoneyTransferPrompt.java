package de.henru.dominoxpgmaing.dominoxp.powersigns.utils;


import de.henru.dominoxpgmaing.dominoxp.powersigns.PowerSigns;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import java.util.HashMap;
import java.util.Map;

public class AcceptMoneyTransferPrompt extends BooleanPrompt {
    private static final String KEY_SOURCE_USER = "source";
    private static final String KEY_DESTINATION_USER = "destination";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_LISTENER = "listener";

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, boolean input) {
        Runnable task = (Runnable) context.getSessionData(KEY_LISTENER);

        if(input){
            context.getForWhom().sendRawMessage("A");
        }else{
            context.getForWhom().sendRawMessage("B");
        }


        if(task!=null){
            Bukkit.getServer().getScheduler().runTaskAsynchronously(PowerSigns.getInstance(), task);
        }
        return null;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "Test String";
    }


    public static Map<Object, Object> generateConversationData(OfflinePlayer source, OfflinePlayer destination, float amount, Runnable transactionListener){
        Map<Object, Object> output = new HashMap<>();

        output.put(KEY_SOURCE_USER, source);
        output.put(KEY_DESTINATION_USER, destination);
        output.put(KEY_AMOUNT, amount);
        output.put(KEY_LISTENER, transactionListener);

        return output;
    }
}
