package org.bukkit.conversations;

import java.util.EventListener;

/**
 */
public interface ConversationAbandonedListener extends EventListener {
    /**
     * Called whenever a {@link Conversation} is abandoned.
     * @param abandonedEvent Contains details about the abandoned conversation.
     */
    public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent);
}
