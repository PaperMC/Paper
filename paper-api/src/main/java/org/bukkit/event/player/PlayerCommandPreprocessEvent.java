package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called whenever a player runs a command (by placing a slash
 * at the start of their message). It is called early in the command handling
 * process, and modifications in this event (via {@link #setMessage(String)})
 * will be shown in the behavior.
 * <p>
 * Many plugins will have <b>no use for this event</b>, and you should
 * attempt to avoid using it if it is not necessary.
 * <p>
 * Some examples of valid uses for this event are:
 * <ul>
 * <li>Logging executed commands to a separate file
 * <li>Variable substitution. For example, replacing
 *     <code>${nearbyPlayer}</code> with the name of the nearest other
 *     player, or simulating the <code>@a</code> and <code>@p</code>
 *     decorators used by Command Blocks in plugins that do not handle it.
 * <li>Conditionally blocking commands belonging to other plugins. For
 *     example, blocking the use of the <code>/home</code> command in a
 *     combat arena.
 * <li>Per-sender command aliases. For example, after a player runs the
 *     command <code>/calias cr gamemode creative</code>, the next time they
 *     run <code>/cr</code>, it gets replaced into
 *     <code>/gamemode creative</code>. (Global command aliases should be
 *     done by registering the alias.)
 * </ul>
 * <p>
 * Examples of incorrect uses are:
 * <ul>
 * <li>Using this event to run command logic
 * </ul>
 * <p>
 * If the event is cancelled, processing of the command will halt.
 * <p>
 * The state of whether or not there is a slash (<code>/</code>) at the
 * beginning of the message should be preserved. If a slash is added or
 * removed, unexpected behavior may result.
 *
 * @since 1.0.0 R1
 */
public class PlayerCommandPreprocessEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private String message;
    private final Set<Player> recipients;

    public PlayerCommandPreprocessEvent(@NotNull final Player player, @NotNull final String message) {
        super(player);
        this.recipients = new HashSet<Player>(player.getServer().getOnlinePlayers());
        this.message = message;
    }

    public PlayerCommandPreprocessEvent(@NotNull final Player player, @NotNull final String message, @NotNull final Set<Player> recipients) {
        super(player);
        this.recipients = recipients;
        this.message = message;
    }

    /**
     * @since 1.3.1 R1.0
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * @since 1.3.1 R1.0
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the command that the player is attempting to send.
     * <p>
     * All commands begin with a special character; implementations do not
     * consider the first character when executing the content.
     *
     * @return Message the player is attempting to send
     * @since 1.3.1 R1.0
     */
    @NotNull
    public String getMessage() {
        return message;
    }

    /**
     * Sets the command that the player will send.
     * <p>
     * All commands begin with a special character; implementations do not
     * consider the first character when executing the content.
     *
     * @param command New message that the player will send
     * @throws IllegalArgumentException if command is null or empty
     * @since 1.3.1 R1.0
     */
    public void setMessage(@NotNull String command) throws IllegalArgumentException {
        Preconditions.checkArgument(command != null, "Command cannot be null");
        Preconditions.checkArgument(!command.isEmpty(), "Command cannot be empty");
        this.message = command;
    }

    /**
     * Sets the player that this command will be executed as.
     *
     * @param player New player which this event will execute as
     * @throws IllegalArgumentException if the player provided is null
     * @deprecated Only works for sign commands; use {@link Player#performCommand(String)}, including those cases
     */
    @Deprecated(forRemoval = true)
    public void setPlayer(@NotNull final Player player) throws IllegalArgumentException {
        Preconditions.checkArgument(player != null, "Player cannot be null");
        this.player = player;
    }

    /**
     * Gets a set of recipients that this chat message will be displayed to.
     * <p>
     * The set returned is not guaranteed to be mutable and may auto-populate
     * on access. Any listener accessing the returned set should be aware that
     * it may reduce performance for a lazy set implementation. Listeners
     * should be aware that modifying the list may throw {@link
     * UnsupportedOperationException} if the event caller provides an
     * unmodifiable set.
     *
     * @return All Players who will see this chat message
     * @deprecated This is simply the online players. Modifications have no effect
     */
    @NotNull
    @Deprecated(since = "1.3.1", forRemoval = true)
    public Set<Player> getRecipients() {
        return recipients;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
