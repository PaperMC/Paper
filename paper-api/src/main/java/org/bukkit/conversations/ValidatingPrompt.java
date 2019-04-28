package org.bukkit.conversations;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ValidatingPrompt is the base class for any prompt that requires validation.
 * ValidatingPrompt will keep replaying the prompt text until the user enters
 * a valid response.
 */
public abstract class ValidatingPrompt implements Prompt {
    public ValidatingPrompt() {
        super();
    }

    /**
     * Accepts and processes input from the user and validates it. If
     * validation fails, this prompt is returned for re-execution, otherwise
     * the next Prompt in the prompt graph is returned.
     *
     * @param context Context information about the conversation.
     * @param input The input text from the user.
     * @return This prompt or the next Prompt in the prompt graph.
     */
    @Override
    @Nullable
    public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        if (isInputValid(context, input)) {
            return acceptValidatedInput(context, input);
        } else {
            String failPrompt = getFailedValidationText(context, input);
            if (failPrompt != null) {
                context.getForWhom().sendRawMessage(ChatColor.RED + failPrompt);
            }
            // Redisplay this prompt to the user to re-collect input
            return this;
        }
    }

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

    /**
     * Override this method to check the validity of the player's input.
     *
     * @param context Context information about the conversation.
     * @param input The player's raw console input.
     * @return True or false depending on the validity of the input.
     */
    protected abstract boolean isInputValid(@NotNull ConversationContext context, @NotNull String input);

    /**
     * Override this method to accept and processes the validated input from
     * the user. Using the input, the next Prompt in the prompt graph should
     * be returned.
     *
     * @param context Context information about the conversation.
     * @param input The validated input text from the user.
     * @return The next Prompt in the prompt graph.
     */
    @Nullable
    protected abstract Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input);

    /**
     * Optionally override this method to display an additional message if the
     * user enters an invalid input.
     *
     * @param context Context information about the conversation.
     * @param invalidInput The invalid input provided by the user.
     * @return A message explaining how to correct the input.
     */
    @Nullable
    protected String getFailedValidationText(@NotNull ConversationContext context, @NotNull String invalidInput) {
        return null;
    }
}
