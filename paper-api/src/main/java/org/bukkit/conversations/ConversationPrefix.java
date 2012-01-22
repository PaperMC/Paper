package org.bukkit.conversations;

import org.bukkit.command.CommandSender;

/**
 * A ConversationPrefix implementation prepends all output from the conversation to the player.
 * The ConversationPrefix can be used to display the plugin name or conversation status as the conversation evolves.
 */
public interface ConversationPrefix {

    /**
     * Gets the prefix to use before each message to the player.
     * @param context Context information about the conversation.
     * @return The prefix text.
     */
    String getPrefix(ConversationContext context);
}
