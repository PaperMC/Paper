package io.papermc.paper.event.connection.common;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.connection.PlayerConnection;
import io.papermc.paper.connection.PlayerLoginConnection;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Objects;

/**
 * Called when a player's connection phase is effectively "finished", meaning
 * that they are progressing to another connection stage.
 * <p>
 * Currently fires during the login and configuration protocol.
 */
@NullMarked
public class PlayerFinishConnectionPhaseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private PlayerConnection currentConnection;

    @Nullable
    private Component kickMessage;

    @ApiStatus.Internal
    public PlayerFinishConnectionPhaseEvent(PlayerConnection connection) {
        super(false);
        this.currentConnection = connection;
    }

    /**
     * Gets the current connection.
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
    public void deny(Component component) {
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
     * @return kick message
     */
    @org.jetbrains.annotations.Nullable
    public Component getKickMessage() {
        return this.kickMessage;
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
