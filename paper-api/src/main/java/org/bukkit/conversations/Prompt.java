package org.bukkit.conversations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A Prompt is the main constituent of a {@link Conversation}. Each prompt
 * displays text to the user and optionally waits for a user's response.
 * Prompts are chained together into a directed graph that represents the
 * conversation flow. To halt a conversation, END_OF_CONVERSATION is returned
 * in liu of another Prompt object.
 */
public interface Prompt extends Cloneable {

    /**
     * A convenience constant for indicating the end of a conversation.
     */
    static final Prompt END_OF_CONVERSATION = null;

    /**
     * Gets the text to display to the user when this prompt is first
     * presented.
     *
     * @param context Context information about the conversation.
     * @return The text to display.
     */
    @NotNull
    String getPromptText(@NotNull ConversationContext context);

    /**
     * Checks to see if this prompt implementation should wait for user input
     * or immediately display the next prompt.
     *
     * @param context Context information about the conversation.
     * @return If true, the {@link Conversation} will wait for input before
     *     continuing. If false, {@link #acceptInput(ConversationContext, String)} will be called immediately with {@code null} input.
     */
    boolean blocksForInput(@NotNull ConversationContext context);

    /**
     * Accepts and processes input from the user. Using the input, the next
     * Prompt in the prompt graph is returned.
     *
     * @param context Context information about the conversation.
     * @param input The input text from the user.
     * @return The next Prompt in the prompt graph.
     */
    @Nullable
    Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input);
}
