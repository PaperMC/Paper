package org.bukkit.conversations;

import java.util.EventObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ConversationAbandonedEvent contains information about an abandoned
 * conversation.
 *
 * @deprecated The conversation API has been deprecated for removal. This system does not support component based messages
 * and has been slowly losing functionality over the years as Minecraft has changed that this API can not adapt to.
 * It is recommended you instead manually listen to the {@link io.papermc.paper.event.player.AsyncChatEvent}
 * or alternatively using {@link io.papermc.paper.dialog.Dialog} to get user input.
 */
@Deprecated(forRemoval = true)
public class ConversationAbandonedEvent extends EventObject {

    private ConversationContext context;
    private ConversationCanceller canceller;

    public ConversationAbandonedEvent(@NotNull Conversation conversation) {
        this(conversation, null);
    }

    public ConversationAbandonedEvent(@NotNull Conversation conversation, @Nullable ConversationCanceller canceller) {
        super(conversation);
        this.context = conversation.getContext();
        this.canceller = canceller;
    }

    /**
     * Gets the object that caused the conversation to be abandoned.
     *
     * @return The object that abandoned the conversation.
     */
    @Nullable
    public ConversationCanceller getCanceller() {
        return canceller;
    }

    /**
     * Gets the abandoned conversation's conversation context.
     *
     * @return The abandoned conversation's conversation context.
     */
    @NotNull
    public ConversationContext getContext() {
        return context;
    }

    /**
     * Indicates how the conversation was abandoned - naturally as part of the
     * prompt chain or prematurely via a {@link ConversationCanceller}.
     *
     * @return True if the conversation is abandoned gracefully by a {@link
     *     Prompt} returning null or the next prompt. False of the
     *     conversations is abandoned prematurely by a ConversationCanceller.
     */
    public boolean gracefulExit() {
        return canceller == null;
    }
}
