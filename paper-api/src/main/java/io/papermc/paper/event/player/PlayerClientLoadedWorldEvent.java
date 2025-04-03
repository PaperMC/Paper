package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player is marked as loaded.
 * <p>
 * This either happens when the player notifies the server after loading the world (closing the downloading terrain screen)
 * or when the player has not done so for 60 ticks after joining the server or respawning.
 */
@NullMarked
public class PlayerClientLoadedWorldEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean timeout;

    @ApiStatus.Internal
    public PlayerClientLoadedWorldEvent(final Player player, final boolean timeout) {
        super(player);
        this.timeout = timeout;
    }

    /**
     * True if the event was triggered because the server has not been notified by the player
     * for 60 ticks after the player joined the server or respawned.
     *
     * @return true if the event was triggered because of a timeout
     */
    public boolean isTimeout() {
        return timeout;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
