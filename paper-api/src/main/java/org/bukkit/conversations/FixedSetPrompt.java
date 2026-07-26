package org.bukkit.conversations;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * FixedSetPrompt is the base class for any prompt that requires a fixed set
 * response from the user.
 *
 * @deprecated The conversation API has been deprecated for removal. This system does not support component based messages
 * and has been slowly losing functionality over the years as Minecraft has changed that this API can not adapt to.
 * It is recommended you instead manually listen to the {@link io.papermc.paper.event.player.AsyncChatEvent}
 * or alternatively using {@link io.papermc.paper.dialog.Dialog} to get user input.
 */
@Deprecated(forRemoval = true)
public abstract class FixedSetPrompt extends ValidatingPrompt {

    protected List<String> fixedSet;

    /**
     * Creates a FixedSetPrompt from a set of strings.
     * <p>
     * foo = new FixedSetPrompt("bar", "cheese", "panda");
     *
     * @param fixedSet A fixed set of strings, one of which the user must
     *     type.
     */
    public FixedSetPrompt(@NotNull String... fixedSet) {
        super();
        this.fixedSet = Arrays.asList(fixedSet);
    }

    private FixedSetPrompt() {}

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return fixedSet.contains(input);
    }

    /**
     * Utility function to create a formatted string containing all the
     * options declared in the constructor.
     *
     * @return the options formatted like "[bar, cheese, panda]" if bar,
     *     cheese, and panda were the options used
     */
    @NotNull
    protected String formatFixedSet() {
        return "[" + Joiner.on(", ").join(fixedSet) + "]";
    }
}
