package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;

/**
 * StringPrompt is the base class for any prompt that accepts an arbitrary
 * string from the user.
 *
 * @deprecated The conversation API has been deprecated for removal. This system does not support component based messages
 * and has been slowly losing functionality over the years as Minecraft has changed that this API can not adapt to.
 * It is recommended you instead manually listen to the {@link io.papermc.paper.event.player.AsyncChatEvent}
 * or alternatively using {@link io.papermc.paper.dialog.Dialog} to get user input.
 */
@Deprecated(forRemoval = true)
public abstract class StringPrompt implements Prompt {

    /**
     * Ensures that the prompt waits for the user to provide input.
     *
     * @param context Context information about the conversation.
     * @return True.
     */
    @Override
    public boolean blocksForInput(@NotNull ConversationContext context) {
        return true;
    }
}
