package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 *
 * @author azi
 */
public class PlayerToggleSneakEvent extends PlayerEvent implements Cancellable {
    private boolean isSneaking;
    private boolean cancel = false;

    public PlayerToggleSneakEvent(final Player player, boolean isSneaking) {
        super(Type.PLAYER_TOGGLE_SNEAK, player);
        this.isSneaking = isSneaking;
    }

    /**
     * Returns whether the player is now sneaking.
     * 
     * @return sneaking state
     */
    public boolean isSneaking() {
        return isSneaking;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
