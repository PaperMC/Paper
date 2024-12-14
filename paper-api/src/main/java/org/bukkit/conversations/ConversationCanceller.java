package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;

/**
 * A ConversationCanceller is a class that cancels an active {@link
 * Conversation}. A Conversation can have more than one ConversationCanceller.
 *
 * @since 1.1.0 R5
 */
public interface ConversationCanceller extends Cloneable {

    /**
     * Sets the conversation this ConversationCanceller can optionally cancel.
     *
     * @param conversation A conversation.
     */
    public void setConversation(@NotNull Conversation conversation);

    /**
     * Cancels a conversation based on user input.
     *
     * @param context Context information about the conversation.
     * @param input The input text from the user.
     * @return True to cancel the conversation, False otherwise.
     */
    public boolean cancelBasedOnInput(@NotNull ConversationContext context, @NotNull String input);

    /**
     * Allows the {@link ConversationFactory} to duplicate this
     * ConversationCanceller when creating a new {@link Conversation}.
     * <p>
     * Implementing this method should reset any internal object state.
     *
     * @return A clone.
     */
    @NotNull
    public ConversationCanceller clone();
}
