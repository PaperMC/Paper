package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;

/**
 * The ManuallyAbandonedConversationCanceller is only used as part of a {@link
 * ConversationAbandonedEvent} to indicate that the conversation was manually
 * abandoned by programmatically calling the abandon() method on it.
 */
public class ManuallyAbandonedConversationCanceller implements ConversationCanceller {
    public void setConversation(@NotNull Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    public boolean cancelBasedOnInput(@NotNull ConversationContext context, @NotNull String input) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public ConversationCanceller clone() {
        throw new UnsupportedOperationException();
    }
}
