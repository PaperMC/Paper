package org.bukkit.conversations;

import java.util.regex.Pattern;

/**
 * RegexPrompt is the base class for any prompt that requires an input validated by a regular expression.
 */
public abstract class RegexPrompt extends ValidatingPrompt {

    private Pattern pattern;

    public RegexPrompt(String regex) {
        this(Pattern.compile(regex));
    }

    public RegexPrompt(Pattern pattern) {
        super();
        this.pattern = pattern;
    }

    private RegexPrompt() {}

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return pattern.matcher(input).matches();
    }
}
