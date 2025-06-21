package io.papermc.paper.event.connection.common;

import io.papermc.paper.connection.PlayerConnection;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player's connection phase is effectively "finished", meaning
 * that they are progressing to another connection stage.
 * <p>
 * All connection related conditions are done before this event, meaning that this event
 * is the LAST determiner if a player progresses onto the other connection stage.
 * <p>
 * Currently fires during the login and configuration protocol.
 */
public class PlayerFinishConnectionPhaseEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerConnection currentConnection;
    private @Nullable Component kickMessage;

    @ApiStatus.Internal
    public PlayerFinishConnectionPhaseEvent(final PlayerConnection connection) {
        super(false);
        this.currentConnection = connection;
    }

    /**
     * Gets the current connection.
     *
     * @return connection
     */
    public PlayerConnection getCurrentConnection() {
        return this.currentConnection;
    }

    /**
     * Rejects the player from entering the next stage.
     *
     * @param component reason
     */
    public void deny(final Component component) {
        this.kickMessage = component;
    }

    /**
     * Allows the player to enter the next stage.
     */
    public void allow() {
        this.kickMessage = null;
    }

    /**
     * Gets if the player is allowed to enter the next stage.
     *
     * @return if allowed
     */
    public boolean isAllowed() {
        return this.kickMessage == null;
    }

    /**
     * Gets the kick message for this event, may be null
     *
     * @return kick message
     */
    public @Nullable Component getKickMessage() {
        return this.kickMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
