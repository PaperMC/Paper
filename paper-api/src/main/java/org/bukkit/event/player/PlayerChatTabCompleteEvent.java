package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import java.util.Collection;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player attempts to tab-complete a chat message.
 *
 * @deprecated This event is no longer fired due to client changes
 */
@Deprecated(since = "1.13")
@Warning(reason = "This event is no longer fired due to client changes")
public class PlayerChatTabCompleteEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String message;
    private final String lastToken;
    private final Collection<String> completions;

    @ApiStatus.Internal
    public PlayerChatTabCompleteEvent(@NotNull final Player player, @NotNull final String message, @NotNull final Collection<String> completions) {
        super(player);
        Preconditions.checkArgument(message != null, "Message cannot be null");
        Preconditions.checkArgument(completions != null, "Completions cannot be null");
        this.message = message;
        int i = message.lastIndexOf(' ');
        if (i < 0) {
            this.lastToken = message;
        } else {
            this.lastToken = message.substring(i + 1);
        }
        this.completions = completions;
    }

    /**
     * Gets the chat message being tab-completed.
     *
     * @return the chat message
     */
    @NotNull
    public String getChatMessage() {
        return this.message;
    }

    /**
     * Gets the last 'token' of the message being tab-completed.
     * <p>
     * The token is the substring starting with the character after the last
     * space in the message.
     *
     * @return The last token for the chat message
     */
    @NotNull
    public String getLastToken() {
        return this.lastToken;
    }

    /**
     * This is the collection of completions for this event.
     *
     * @return the current completions
     */
    @NotNull
    public Collection<String> getTabCompletions() {
        return this.completions;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
