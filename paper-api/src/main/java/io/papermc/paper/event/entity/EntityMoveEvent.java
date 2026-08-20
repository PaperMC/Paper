package io.papermc.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Holds information for living entity movement events
 * <p>
 * Does not fire for players; use {@link PlayerMoveEvent} for player movement.
 */
public interface EntityMoveEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Gets the location this entity moved from
     *
     * @return Location the entity moved from
     */
    Location getFrom();

    /**
     * Sets the location to mark as where the entity moved from
     *
     * @param from New location to mark as the entity's previous location
     */
    void setFrom(Location from);

    /**
     * Gets the location this entity moved to
     *
     * @return Location the entity moved to
     */
    Location getTo();

    /**
     * Sets the location that this entity will move to
     *
     * @param to New Location this entity will move to
     */
    void setTo(Location to);

    /**
     * Check if the entity has changed position (even within the same block) in the event
     *
     * @return whether the entity has changed position or not
     */
    boolean hasChangedPosition();

    /**
     * Check if the entity has changed position (even within the same block) in the event, disregarding a possible world change
     *
     * @return whether the entity has changed position or not
     */
    boolean hasExplicitlyChangedPosition();

    /**
     * Check if the entity has moved to a new block in the event
     *
     * @return whether the entity has moved to a new block or not
     */
    boolean hasChangedBlock();

    /**
     * Check if the entity has moved to a new block in the event, disregarding a possible world change
     *
     * @return whether the entity has moved to a new block or not
     */
    boolean hasExplicitlyChangedBlock();

    /**
     * Check if the entity has changed orientation in the event
     *
     * @return whether the entity has changed orientation or not
     */
    boolean hasChangedOrientation();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
