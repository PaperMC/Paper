package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MessagePrompt is the base class for any prompt that only displays a message
 * to the user and requires no input.
 */
public abstract class MessagePrompt implements Prompt {

    public MessagePrompt() {
        super();
    }

    /**
     * Message prompts never wait for user input before continuing.
     *
     * @param context Context information about the conversation.
     * @return Always false.
     */
    @Override
    public boolean blocksForInput(@NotNull ConversationContext context) {
        return false;
    }

    /**
     * Accepts and ignores any user input, returning the next prompt in the
     * prompt graph instead.
     *
     * @param context Context information about the conversation.
     * @param input Ignored.
     * @return The next prompt in the prompt graph.
     */
    @Override
    @Nullable
    public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        return getNextPrompt(context);
    }

    /**
     * Override this method to return the next prompt in the prompt graph.
     *
     * @param context Context information about the conversation.
     * @return The next prompt in the prompt graph.
     */
    @Nullable
    protected abstract Prompt getNextPrompt(@NotNull ConversationContext context);
}
