package org.bukkit.event.server;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class TabCompleteEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final CommandSender sender;
    private final String buffer;
    private final boolean isCommand;
    private final Location location;
    private List<String> completions;

    private boolean cancelled;

    @ApiStatus.Internal
    public TabCompleteEvent(@NotNull CommandSender sender, @NotNull String buffer, @NotNull List<String> completions) {
        this(sender, buffer, completions, sender instanceof ConsoleCommandSender || buffer.startsWith("/"), null);
    }

    @ApiStatus.Internal
    public TabCompleteEvent(@NotNull CommandSender sender, @NotNull String buffer, @NotNull List<String> completions, boolean isCommand, @Nullable Location location) {
        this.sender = sender;
        this.buffer = buffer;
        this.completions = new ArrayList<>(completions);
        this.isCommand = isCommand;
        this.location = location;
    }

    /**
     * Get the sender completing this command.
     *
     * @return the {@link CommandSender} instance
     */
    @NotNull
    public CommandSender getSender() {
        return this.sender;
    }

    /**
     * Return the entire buffer which formed the basis of this completion.
     *
     * @return command buffer, as entered
     */
    @NotNull
    public String getBuffer() {
        return this.buffer;
    }

    /**
     * The list of completions which will be offered to the sender. Completions may be ordered alphanumerically later on in the tab completion process.
     * This list is mutable and reflects what will be offered.
     *
     * @return a list of offered completions
     */
    @NotNull
    public List<String> getCompletions() {
        return this.completions;
    }

    /**
     * Set the completions offered, overriding any already set.
     * <br>
     * The passed collection will be cloned to a new List. You must call {@link #getCompletions()} to mutate from here
     *
     * @param completions the new completions
     */
    public void setCompletions(@NotNull List<String> completions) {
        Preconditions.checkArgument(completions != null, "completions cannot be null");
        this.completions = new ArrayList<>(completions);
    }

    /**
     * @return {@code true} if it is a command being tab completed, {@code false} if it is a chat message.
     */
    public boolean isCommand() {
        return this.isCommand;
    }

    /**
     * @return The position looked at by the sender, or {@code null} if none
     */
    @Nullable
    public Location getLocation() {
        return this.location != null ? this.location.clone() : null;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
