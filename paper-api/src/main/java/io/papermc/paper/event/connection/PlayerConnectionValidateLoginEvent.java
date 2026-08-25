package io.papermc.paper.event.connection;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.connection.PlayerConnection;
import io.papermc.paper.connection.PlayerLoginConnection;
import net.kyori.adventure.text.Component;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Validates whether a player connection is able to log in.
 * <p>
 * Called when a player is attempting to log in for the first time,
 * or is finishing up being configured.
 * <p>
 * However, these underlying connection types are not guaranteed to be stable across versions.
 * <p>
 * Note: Since 1.21.7, during these phases, the {@link #getConnection() connection}
 * is either {@link PlayerLoginConnection} or {@link PlayerConfigurationConnection},
 * allowing access to phase-specific API.
 */
public interface PlayerConnectionValidateLoginEvent extends ConnectionEvent {

    /**
     * Gets the connection of the player in this event.
     * <p>
     * However, these underlying connection types are not guaranteed to be stable across versions.
     * <p>
     * Note: Since 1.21.7, this connection is either
     * {@link PlayerLoginConnection} or {@link PlayerConfigurationConnection}
     * depending on which phase it is fired in, allowing access to phase-specific API.
     *
     * @return connection
     * @apiNote disconnecting the player through this connection
     * or using any methods that may send packets is not supported
     */
    @Override
    PlayerConnection getConnection();

    /**
     * Gets the reason for why a player is not allowed to join the server.
     * This will be null in the case that the player is allowed to log in.
     *
     * @return disallow reason
     */
    @Nullable Component getKickMessage();

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param message Kick message to display to the user
     */
    void kickMessage(Component message);

    /**
     * Gets if the player is allowed to enter the next stage.
     *
     * @return if allowed
     */
    boolean isAllowed();

    /**
     * Allows the player to log in.
     * This skips any login validation checks.
     */
    void allow();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
