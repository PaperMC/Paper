package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;

/**
 * NullConversationPrefix is a {@link ConversationPrefix} implementation that
 * displays nothing in front of conversation output.
 */
public class NullConversationPrefix implements ConversationPrefix {

    /**
     * Prepends each conversation message with an empty string.
     *
     * @param context Context information about the conversation.
     * @return An empty string.
     */
    @Override
    @NotNull
    public String getPrefix(@NotNull ConversationContext context) {
        return "";
    }
}
