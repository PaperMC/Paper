package org.bukkit.conversations;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * BooleanPrompt is the base class for any prompt that requires a boolean
 * response from the user.
 */
public abstract class BooleanPrompt extends ValidatingPrompt {

    public BooleanPrompt() {
        super();
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        String[] accepted = {/* Apache values: */"true", "false", "on", "off", "yes", "no",/* Additional values: */ "y", "n", "1", "0", "right", "wrong", "correct", "incorrect", "valid", "invalid"};
        return ArrayUtils.contains(accepted, input.toLowerCase());
    }

    @Nullable
    @Override
    protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        if (input.equalsIgnoreCase("y") || input.equals("1") || input.equalsIgnoreCase("right") || input.equalsIgnoreCase("correct") || input.equalsIgnoreCase("valid")) input = "true";
        return acceptValidatedInput(context, BooleanUtils.toBoolean(input));
    }

    /**
     * Override this method to perform some action with the user's boolean
     * response.
     *
     * @param context Context information about the conversation.
     * @param input The user's boolean response.
     * @return The next {@link Prompt} in the prompt graph.
     */
    @Nullable
    protected abstract Prompt acceptValidatedInput(@NotNull ConversationContext context, boolean input);
}
