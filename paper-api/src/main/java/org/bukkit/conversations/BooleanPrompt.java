package org.bukkit.conversations;

import com.google.common.collect.ImmutableSet;
import java.util.Locale;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * BooleanPrompt is the base class for any prompt that requires a boolean
 * response from the user.
 */
public abstract class BooleanPrompt extends ValidatingPrompt {

    private static final Set<String> TRUE_INPUTS = ImmutableSet.of("true", "on", "yes", "y", "1", "right", "correct", "valid");
    private static final Set<String> FALSE_INPUTS = ImmutableSet.of("false", "off", "no", "n", "0", "wrong", "incorrect", "invalid");
    private static final Set<String> VALID_INPUTS = ImmutableSet.<String>builder().addAll(TRUE_INPUTS).addAll(FALSE_INPUTS).build();

    public BooleanPrompt() {
        super();
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return VALID_INPUTS.contains(input.toLowerCase(Locale.ROOT));
    }

    @Nullable
    @Override
    protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        return acceptValidatedInput(context, TRUE_INPUTS.contains(input.toLowerCase(Locale.ROOT)));
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
