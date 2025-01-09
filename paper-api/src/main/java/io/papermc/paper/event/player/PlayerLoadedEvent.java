package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player is set as loaded. This either happens when the player sends the player loaded packet after loading the world (closing the downloading terrain screen) or when the player
 * has not sent the packet for 60 ticks after joining the server or respawning.
 */
@NullMarked
public class PlayerLoadedEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Cause cause;

    @ApiStatus.Internal
    public PlayerLoadedEvent(final @NotNull Player who, final Cause cause) {
        super(who);
        this.cause = cause;
    }

    /**
     * The cause of the player load.
     *
     * @return the cause
     */
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * The cause of the {@link PlayerLoadedEvent}.
     */
    public enum Cause {

        /**
         * The event was triggered by the player sending the player loaded packet.
         */
        PACKET,

        /**
         * The event was triggered because the server has not received the player loaded packet for 60 ticks after the player joined the server or respawned.
         */
        TIMEOUT

    }

}
