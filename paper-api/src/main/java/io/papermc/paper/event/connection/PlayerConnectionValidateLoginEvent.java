package io.papermc.paper.event.connection;

import io.papermc.paper.connection.PlayerConnection;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * Validates whether a player connection is able to log in.
 * <p>
 * Called when is attempting to log in for the first time, or is finishing up
 * being configured.
 */
public class PlayerConnectionValidateLoginEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerConnection connection;
    private @Nullable Component kickMessage;

    @ApiStatus.Internal
    public PlayerConnectionValidateLoginEvent(final PlayerConnection connection, final @Nullable Component kickMessage) {
        super(false);
        this.connection = connection;
        this.kickMessage = kickMessage;
    }

    /**
     * Gets the connection of the player in this event.
     * Note, the type of this connection is not guaranteed to be stable across versions.
     * Additionally, disconnecting the player through this connection / using any methods that may send packets
     * is not supported.
     *
     * @return connection
     */
    public PlayerConnection getConnection() {
        return this.connection;
    }

    /**
     * Allows the player to log in.
     * This skips any login validation checks.
     */
    public void allow() {
        this.kickMessage = null;
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param message Kick message to display to the user
     */
    public void kickMessage(final Component message) {
        this.kickMessage = message;
    }

    /**
     * Gets the reason for why a player is not allowed to join the server.
     * This will be null in the case that the player is allowed to log in.
     *
     * @return disallow reason
     */
    public @Nullable Component getKickMessage() {
        return this.kickMessage;
    }

    /**
     * Gets if the player is allowed to enter the next stage.
     *
     * @return if allowed
     */
    public boolean isAllowed() {
        return this.kickMessage == null;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
