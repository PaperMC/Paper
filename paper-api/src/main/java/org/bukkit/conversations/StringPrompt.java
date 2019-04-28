package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;

/**
 * StringPrompt is the base class for any prompt that accepts an arbitrary
 * string from the user.
 */
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
