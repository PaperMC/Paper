package org.bukkit.conversations;

/**
 * A ConversationCanceller is a class that cancels an active {@link Conversation}. A Conversation can have more
 * than one ConversationCanceller.
 */
public interface ConversationCanceller extends Cloneable {

    /**
     * Sets the conversation this ConversationCanceller can optionally cancel.
     * @param conversation A conversation.
     */
    public void setConversation(Conversation conversation);

    /**
     * Cancels a conversation based on user input/
     * @param context Context information about the conversation.
     * @param input The input text from the user.
     * @return True to cancel the conversation, False otherwise.
     */
    public boolean cancelBasedOnInput(ConversationContext context, String input);

    /**
     * Allows the {@link ConversationFactory} to duplicate this ConversationCanceller when creating a new {@link Conversation}.
     * Implementing this method should reset any internal object state.
     * @return A clone.
     */
    public ConversationCanceller clone();
}
