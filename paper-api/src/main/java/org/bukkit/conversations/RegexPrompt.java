package org.bukkit.conversations;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * RegexPrompt is the base class for any prompt that requires an input
 * validated by a regular expression.
 *
 * @deprecated The conversation API has been deprecated for removal. This system does not support component based messages
 * and has been slowly losing functionality over the years as Minecraft has changed that this API can not adapt to.
 * It is recommended you instead manually listen to the {@link io.papermc.paper.event.player.AsyncChatEvent}
 * or alternatively using {@link io.papermc.paper.dialog.Dialog} to get user input.
 */
@Deprecated(forRemoval = true)
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
