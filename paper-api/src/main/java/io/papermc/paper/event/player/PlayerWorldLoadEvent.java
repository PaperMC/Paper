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
public class PlayerWorldLoadEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean timeout;

    @ApiStatus.Internal
    public PlayerWorldLoadEvent(final Player who, final boolean timeout) {
        super(who);
        this.timeout = timeout;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * True if the event was triggered because the server has not received the player loaded packet for 60 ticks after the player joined the server or respawned.
     *
     * @return true if the event was triggered because of a timeout
     */
    public boolean isTimeout() {
        return timeout;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
