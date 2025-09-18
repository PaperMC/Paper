package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;

/**
 * A ConversationPrefix implementation prepends all output from the
 * conversation to the player. The ConversationPrefix can be used to display
 * the plugin name or conversation status as the conversation evolves.
 *
 * @deprecated The conversation API has been deprecated for removal. This system does not support component based messages
 * and has been slowly losing functionality over the years as Minecraft has changed that this API can not adapt to.
 * It is recommended you instead manually listen to the {@link io.papermc.paper.event.player.AsyncChatEvent}
 * or alternatively using {@link io.papermc.paper.dialog.Dialog} to get user input.
 */
@Deprecated(forRemoval = true)
public interface ConversationPrefix {

    /**
     * Gets the prefix to use before each message to the player.
     *
     * @param context Context information about the conversation.
     * @return The prefix text.
     */
    @NotNull
    String getPrefix(@NotNull ConversationContext context);
}
