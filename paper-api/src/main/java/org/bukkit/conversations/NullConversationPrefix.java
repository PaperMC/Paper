package org.bukkit.conversations;

import org.bukkit.command.CommandSender;

/**
 * NullConversationPrefix is a {@link ConversationPrefix} implementation that displays nothing in front of
 * conversation output.
 */
public class NullConversationPrefix implements ConversationPrefix{

    /**
     * Prepends each conversation message with an empty string.
     * @param context Context information about the conversation.
     * @return An empty string.
     */
    public String getPrefix(ConversationContext context) {
        return "";
    }
}
