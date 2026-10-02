package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;

/// A ConversationCanceller is a class that cancels an active
/// [Conversation]. A Conversation can have more than one ConversationCanceller.
///
/// @deprecated The conversation API has been deprecated for removal. This system does not support component based messages
/// and has been slowly losing functionality over the years as Minecraft has changed that this API can not adapt to.
/// It is recommended you instead manually listen to the [io.papermc.paper.event.player.AsyncChatEvent]
/// or alternatively using [io.papermc.paper.dialog.Dialog] to get user input.
@Deprecated(forRemoval = true)
public interface ConversationCanceller extends Cloneable {

    /// Sets the conversation this ConversationCanceller can optionally cancel.
    ///
    /// @param conversation A conversation.
    public void setConversation(@NotNull Conversation conversation);

    /// Cancels a conversation based on user input.
    ///
    /// @param context Context information about the conversation.
    /// @param input The input text from the user.
    /// @return True to cancel the conversation, False otherwise.
    public boolean cancelBasedOnInput(@NotNull ConversationContext context, @NotNull String input);

    /// Allows the [ConversationFactory] to duplicate this
    /// ConversationCanceller when creating a new [Conversation].
    ///
    /// Implementing this method should reset any internal object state.
    ///
    /// @return A clone.
    @NotNull
    public ConversationCanceller clone();
}
