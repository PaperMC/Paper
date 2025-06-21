package io.papermc.paper.event.connection.common;

import io.papermc.paper.connection.PlayerConnection;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * Validates whether a player connection would be able to login at this event.
 * <p>
 * Currently, this occurs when the player connection is attempting to log in for the first time, or is finishing up
 * being configured.
 */
public class PlayerConnectionValidateLoginEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerConnection connection;
    private @Nullable Component disallowedReason;

    @ApiStatus.Internal
    public PlayerConnectionValidateLoginEvent(final PlayerConnection connection, final @Nullable Component disallowReason) {
        super(false);
        this.connection = connection;
        this.disallowedReason = disallowReason;
    }

    public PlayerConnection getConnection() {
        return this.connection;
    }

    /**
     * Allows the player to log in.
     * This skips any login validation checks.
     */
    public void allow() {
        this.disallowedReason = null;
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param message Kick message to display to the user
     */
    public void disallow(final Component message) {
        this.disallowedReason = message;
    }

    /**
     * Gets the reason for why a player is not allowed to join the server.
     * This will be null in the case that the player is allowed to login.
     *
     * @return disallow reason
     */
    public @Nullable Component getDisallowedReason() {
        return this.disallowedReason;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
