package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * This event is fired when the player is leaving a bed.
 */
public interface PlayerBedLeaveEvent extends PlayerEvent, Cancellable {

    /**
     * Returns the bed block involved in this event.
     *
     * @return the bed block involved in this event
     */
    Block getBed();

    /**
     * Get if this event should set the new spawn location for the
     * {@link Player}.
     * <br>
     * This does not remove any existing spawn location, only prevent it from
     * being changed (if true).
     * <br>
     * To change a {@link Player}'s spawn location, use
     * {@link Player#setBedSpawnLocation(Location)}.
     *
     * @return {@code true} if the spawn location will be changed
     * @deprecated the respawn point is now set when the player enter the bed and
     * this option doesn't work since MC 1.15.
     */
    @Deprecated(forRemoval = true, since = "1.15")
    boolean shouldSetSpawnLocation();

    /**
     * Set if this event should set the new spawn location for the
     * {@link Player}.
     * <br>
     * This will not remove any existing spawn location, only prevent it from
     * being changed (if true).
     * <br>
     * To change a {@link Player}'s spawn location, use
     * {@link Player#setBedSpawnLocation(Location)}.
     *
     * @param setBedSpawn {@code true} to change the new spawn location
     * @deprecated the respawn point is now set when the player enter the bed and
     * this option doesn't work since MC 1.15.
     */
    @Deprecated(forRemoval = true, since = "1.15")
    void setSpawnLocation(boolean setBedSpawn);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
