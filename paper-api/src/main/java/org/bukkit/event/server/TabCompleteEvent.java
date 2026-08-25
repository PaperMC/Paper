package org.bukkit.event.server;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.jspecify.annotations.Nullable;

/**
 * Called when a {@link CommandSender} of any description (ie: player or
 * console) attempts to tab complete.
 * <br>
 * Note that due to client changes, if the sender is a Player, this event will
 * only begin to fire once command arguments are specified, not commands
 * themselves. Plugins wishing to remove commands from tab completion are
 * advised to ensure the client does not have permission for the relevant
 * commands, or use {@link PlayerCommandSendEvent}.
 *
 * @apiNote Only called for bukkit API commands {@link org.bukkit.command.Command} and
 * {@link org.bukkit.command.CommandExecutor} and not for brigadier commands ({@link io.papermc.paper.command.brigadier.Commands}).
 */
public interface TabCompleteEvent extends Event, Cancellable {

    /**
     * Get the sender completing this command.
     *
     * @return the {@link CommandSender} instance
     */
    CommandSender getSender();

    /**
     * Return the entire buffer which formed the basis of this completion.
     *
     * @return command buffer, as entered
     */
    String getBuffer();

    /**
     * The list of completions which will be offered to the sender. Completions may be ordered alphanumerically later on in the tab completion process.
     * This list is mutable and reflects what will be offered.
     *
     * @return a list of offered completions
     */
    List<String> getCompletions();

    /**
     * Set the completions offered, overriding any already set.
     * <br>
     * The passed collection will be cloned to a new List. You must call {@link #getCompletions()} to mutate from here
     *
     * @param completions the new completions
     */
    void setCompletions(List<String> completions);

    /**
     * @return {@code true} if it is a command being tab completed, {@code false} if it is a chat message.
     */
    boolean isCommand();

    /**
     * @return The position looked at by the sender, or {@code null} if none
     */
    @Nullable Location getLocation();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
