package org.bukkit.conversations;

import java.util.EventListener;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.3.1
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
