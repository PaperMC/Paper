package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Holds information for player chat and commands
 *
 * @deprecated This event will fire from the main thread and allows the use of
 *     all of the Bukkit API, unlike the {@link AsyncPlayerChatEvent}.
 *     <p>
 *     Listening to this event forces chat to wait for the main thread which
 *     causes delays for chat. {@link AsyncPlayerChatEvent} is the encouraged
 *     alternative for thread safe implementations.
 */
@Deprecated
@Warning(reason = "Listening to this event forces chat to wait for the main thread, delaying chat messages.")
public class PlayerChatEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private String message;
    private String format;
    private final Set<Player> recipients;

    public PlayerChatEvent(@NotNull final Player player, @NotNull final String message) {
        super(player);
        this.message = message;
        this.format = "<%1$s> %2$s";
        this.recipients = new HashSet<Player>(player.getServer().getOnlinePlayers());
    }

    public PlayerChatEvent(@NotNull final Player player, @NotNull final String message, @NotNull final String format, @NotNull final Set<Player> recipients) {
        super(player);
        this.message = message;
        this.format = format;
        this.recipients = recipients;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the message that the player is attempting to send
     *
     * @return Message the player is attempting to send
     */
    @NotNull
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message that the player will send
     *
     * @param message New message that the player will send
     */
    public void setMessage(@NotNull String message) {
        this.message = message;
    }

    /**
     * Sets the player that this message will display as, or command will be
     * executed as
     *
     * @param player New player which this event will execute as
     */
    public void setPlayer(@NotNull final Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null");
        this.player = player;
    }

    /**
     * Gets the format to use to display this chat message
     *
     * @return String.Format compatible format string
     */
    @NotNull
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format to use to display this chat message
     *
     * @param format String.Format compatible format string
     */
    public void setFormat(@NotNull final String format) {
        // Oh for a better way to do this!
        try {
            String.format(format, player, message);
        } catch (RuntimeException ex) {
            ex.fillInStackTrace();
            throw ex;
        }

        this.format = format;
    }

    /**
     * Gets a set of recipients that this chat message will be displayed to
     *
     * @return All Players who will see this chat message
     */
    @NotNull
    public Set<Player> getRecipients() {
        return recipients;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
