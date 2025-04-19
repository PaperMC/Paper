package io.papermc.paper.event.connection.common;

import io.papermc.paper.connection.PlayerConnection;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

/**
 * Validates whether a player connection would be able to login at this event.
 * <p>
 * Currently, this occurs when the player connection is attempting to log in for the first time, or is finishing up
 * being configured.
 */
@NullMarked
public class PlayerConnectionValidateLoginEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final PlayerConnection connection;

    @Nullable
    private Component disallowedReason;

    @ApiStatus.Internal
    public PlayerConnectionValidateLoginEvent(final PlayerConnection connection, @Nullable final Component disallowReason) {
        super(false);
        this.connection = connection;
        this.disallowedReason = disallowReason;
    }

    public PlayerConnection getConnection() {
        return connection;
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
    public void disallow(@NotNull final net.kyori.adventure.text.Component message) {
        this.disallowedReason = message;
    }

    /**
     * Gets the reason for why a player is not allowed to join the server.
     * This may be null in the case that the player is allowed to login.
     * @return disallow reason
     */
    public @Nullable Component getDisallowedReason() {
        return disallowedReason;
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
