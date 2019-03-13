package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 */
public interface ConversationAbandonedListener extends EventListener {
    /**
     * Called whenever a {@link Conversation} is abandoned.
     *
     * @param abandonedEvent Contains details about the abandoned
     *     conversation.
     */
    public void conversationAbandoned(@NotNull ConversationAbandonedEvent abandonedEvent);
}
