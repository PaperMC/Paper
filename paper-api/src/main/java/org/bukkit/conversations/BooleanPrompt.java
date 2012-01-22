package org.bukkit.conversations;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;

/**
 * BooleanPrompt is the base class for any prompt that requires a boolean response from the user.
 */
public abstract class BooleanPrompt extends ValidatingPrompt{

    public BooleanPrompt() {
        super();
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        String[] accepted = {"true", "false", "on", "off", "yes", "no"};
        return ArrayUtils.contains(accepted, input.toLowerCase());
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        return acceptValidatedInput(context, BooleanUtils.toBoolean(input));
    }

    /**
     * Override this method to perform some action with the user's boolean response.
     * @param context Context information about the conversation.
     * @param input The user's boolean response.
     * @return The next {@link Prompt} in the prompt graph.
     */
    protected abstract Prompt acceptValidatedInput(ConversationContext context, boolean input);
}
