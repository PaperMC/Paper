package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;

/**
 * An ExactMatchConversationCanceller cancels a conversation if the user
 * enters an exact input string
 *
 * @since 1.1.0 R5
 */
public class ExactMatchConversationCanceller implements ConversationCanceller {
    private String escapeSequence;

    /**
     * Builds an ExactMatchConversationCanceller.
     *
     * @param escapeSequence The string that, if entered by the user, will
     *     cancel the conversation.
     */
    public ExactMatchConversationCanceller(@NotNull String escapeSequence) {
        this.escapeSequence = escapeSequence;
    }

    @Override
    public void setConversation(@NotNull Conversation conversation) {}

    @Override
    public boolean cancelBasedOnInput(@NotNull ConversationContext context, @NotNull String input) {
        return input.equals(escapeSequence);
    }

    @Override
    @NotNull
    public ConversationCanceller clone() {
        return new ExactMatchConversationCanceller(escapeSequence);
    }
}
