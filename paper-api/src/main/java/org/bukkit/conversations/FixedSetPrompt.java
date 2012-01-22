package org.bukkit.conversations;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * FixedSetPrompt is the base class for any prompt that requires a fixed set response from the user.
 */
public abstract class FixedSetPrompt extends ValidatingPrompt {
    
    protected List<String> fixedSet;

    /**
     * Creates a FixedSetPrompt from a set of strings.
     * foo = new FixedSetPrompt("bar", "cheese", "panda");
     * @param fixedSet A fixed set of strings, one of which the user must type.
     */
    public FixedSetPrompt(String... fixedSet) {
        super();
        this.fixedSet = Arrays.asList(fixedSet);
    }

    private FixedSetPrompt() {}

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return fixedSet.contains(input);
    }

    /**
     * Utility function to create a formatted string containing all the options declared in the constructor.
     * The result is formatted like "[bar, cheese, panda]"
     * @return
     */
    protected String formatFixedSet() {
        return "[" + StringUtils.join(fixedSet, ", ") + "]";
    }
}
