package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;

/// NullConversationPrefix is a [ConversationPrefix] implementation that
/// displays nothing in front of conversation output.
///
/// @deprecated The conversation API has been deprecated for removal. This system does not support component based messages
/// and has been slowly losing functionality over the years as Minecraft has changed that this API can not adapt to.
/// It is recommended you instead manually listen to the [io.papermc.paper.event.player.AsyncChatEvent]
/// or alternatively using [io.papermc.paper.dialog.Dialog] to get user input.
@Deprecated(forRemoval = true)
public class NullConversationPrefix implements ConversationPrefix {

    /// Prepends each conversation message with an empty string.
    ///
    /// @param context Context information about the conversation.
    /// @return An empty string.
    @Override
    @NotNull
    public String getPrefix(@NotNull ConversationContext context) {
        return "";
    }
}
