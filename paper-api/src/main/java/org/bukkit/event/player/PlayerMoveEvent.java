package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Holds information for player movement events
 */
public interface PlayerMoveEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the location this player moved from
     *
     * @return Location the player moved from
     */
    Location getFrom();

    /**
     * Sets the location to mark as where the player moved from
     *
     * @param from New location to mark as the players previous location
     */
    void setFrom(Location from);

    /**
     * Gets the location this player moved to
     *
     * @return Location the player moved to
     */
    Location getTo();

    /**
     * Sets the location that this player will move to
     *
     * @param to New Location this player will move to
     */
    void setTo(Location to);

    /**
     * Check if the player has changed position (even within the same block) in the event
     *
     * @return whether the player has changed position or not
     */
    boolean hasChangedPosition();

    /**
     * Check if the player has changed position (even within the same block) in the event, disregarding a possible world change
     *
     * @return whether the player has changed position or not
     */
    boolean hasExplicitlyChangedPosition();

    /**
     * Check if the player has moved to a new block in the event
     *
     * @return whether the player has moved to a new block or not
     */
    boolean hasChangedBlock();

    /**
     * Check if the player has moved to a new block in the event, disregarding a possible world change
     *
     * @return whether the player has moved to a new block or not
     */
    boolean hasExplicitlyChangedBlock();

    /**
     * Check if the player has changed orientation in the event
     *
     * @return whether the player has changed orientation or not
     */
    boolean hasChangedOrientation();

    /**
     * {@inheritDoc}
     * <p>
     * If a move or teleport event is cancelled, the player will be moved or
     * teleported back to the Location as defined by getFrom(). This will not
     * fire an event
     */
    @Override
    boolean isCancelled();

    /**
     * {@inheritDoc}
     * <p>
     * If a move or teleport event is cancelled, the player will be moved or
     * teleported back to the Location as defined by getFrom(). This will not
     * fire an event
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
