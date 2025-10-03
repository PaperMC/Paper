package org.bukkit.conversations;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The Conversable interface is used to indicate objects that can have
 * conversations.
 *
 * @deprecated The conversation API has been deprecated for removal. This system does not support component based messages
 * and has been slowly losing functionality over the years as Minecraft has changed that this API can not adapt to.
 * It is recommended you instead manually listen to the {@link io.papermc.paper.event.player.AsyncChatEvent}
 * or alternatively using {@link io.papermc.paper.dialog.Dialog} to get user input.
 */
@Deprecated(forRemoval = true)
public interface Conversable {

    /**
     * Tests to see of a Conversable object is actively engaged in a
     * conversation.
     *
     * @return True if a conversation is in progress
     */
    public boolean isConversing();

    /**
     * Accepts input into the active conversation. If no conversation is in
     * progress, this method does nothing.
     *
     * @param input The input message into the conversation
     */
    public void acceptConversationInput(@NotNull String input);

    /**
     * Enters into a dialog with a Conversation object.
     *
     * @param conversation The conversation to begin
     * @return True if the conversation should proceed, false if it has been
     *     enqueued
     */
    public boolean beginConversation(@NotNull Conversation conversation);

    /**
     * Abandons an active conversation.
     *
     * @param conversation The conversation to abandon
     */
    public void abandonConversation(@NotNull Conversation conversation);

    /**
     * Abandons an active conversation.
     *
     * @param conversation The conversation to abandon
     * @param details Details about why the conversation was abandoned
     */
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent details);

    /**
     * Sends this sender a message raw
     *
     * @param message Message to be displayed
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete // Paper
    public void sendRawMessage(@NotNull String message);

    /**
     * Sends this sender a message raw
     *
     * @param message Message to be displayed
     * @param sender The sender of this message
     * @deprecated sender UUID is ignored
     */
    @Deprecated // Paper
    public void sendRawMessage(@Nullable UUID sender, @NotNull String message);
}
