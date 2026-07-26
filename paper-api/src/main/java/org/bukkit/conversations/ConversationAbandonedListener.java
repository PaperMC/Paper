package org.bukkit.conversations;

import java.util.EventListener;
import org.jetbrains.annotations.NotNull;

/**
 * @deprecated The conversation API has been deprecated for removal. This system does not support component based messages
 * and has been slowly losing functionality over the years as Minecraft has changed that this API can not adapt to.
 * It is recommended you instead manually listen to the {@link io.papermc.paper.event.player.AsyncChatEvent}
 * or alternatively using {@link io.papermc.paper.dialog.Dialog} to get user input.
 */
@Deprecated(forRemoval = true)
public interface ConversationAbandonedListener extends EventListener {
    /**
     * Called whenever a {@link Conversation} is abandoned.
     *
     * @param abandonedEvent Contains details about the abandoned
     *     conversation.
     */
    public void conversationAbandoned(@NotNull ConversationAbandonedEvent abandonedEvent);
}
