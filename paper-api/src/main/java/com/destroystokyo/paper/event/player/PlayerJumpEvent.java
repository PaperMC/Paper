package com.destroystokyo.paper.event.player;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the server detects the player is jumping.
 * <p>
 * Added to avoid the overhead and special case logic that many plugins use
 * when checking for jumps via {@link PlayerMoveEvent}, this event is fired whenever
 * the server detects that the player is jumping.
 */
@NullMarked
public interface PlayerJumpEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the location this player jumped from
     *
     * @return Location the player jumped from
     */
    Location getFrom();

    /**
     * Sets the location to mark as where the player jumped from
     *
     * @param from New location to mark as the players previous location
     */
    void setFrom(Location from);

    /**
     * Gets the location this player jumped to
     * <p>
     * This information is based on what the client sends, it typically
     * has little relation to the arc of the jump at any given point.
     *
     * @return Location the player jumped to
     */
    Location getTo();

    /**
     * {@inheritDoc}
     * <p>
     * If this event is cancelled, the player will be moved or
     * teleported back to the Location as defined by {@link #getFrom()}. This will not
     * fire an event
     */
    @Override
    boolean isCancelled();

    /**
     * {@inheritDoc}
     * <p>
     * If this event is cancelled, the player will be moved or
     * teleported back to the Location as defined by {@link #getFrom()}. This will not
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
