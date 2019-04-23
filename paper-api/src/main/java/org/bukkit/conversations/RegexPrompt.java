package org.bukkit.conversations;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * RegexPrompt is the base class for any prompt that requires an input
 * validated by a regular expression.
 */
public abstract class RegexPrompt extends ValidatingPrompt {

    private Pattern pattern;

    public RegexPrompt(@NotNull String regex) {
        this(Pattern.compile(regex));
    }

    public RegexPrompt(@NotNull Pattern pattern) {
        super();
        this.pattern = pattern;
    }

    private RegexPrompt() {}

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return pattern.matcher(input).matches();
    }
}
