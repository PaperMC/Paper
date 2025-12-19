package com.destroystokyo.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the server detects the player is jumping.
 * <p>
 * Added to avoid the overhead and special case logic that many plugins use
 * when checking for jumps via {@link PlayerMoveEvent}, this event is fired whenever
 * the server detects that the player is jumping.
 */
@NullMarked
public class PlayerJumpEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location to;
    private Location from;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerJumpEvent(final Player player, final Location from, final Location to) {
        super(player);
        this.from = from;
        this.to = to;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If this event is cancelled, the player will be moved or
     * teleported back to the Location as defined by {@link #getFrom()}. This will not
     * fire an event
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If this event is cancelled, the player will be moved or
     * teleported back to the Location as defined by {@link #getFrom()}. This will not
     * fire an event
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the location this player jumped from
     *
     * @return Location the player jumped from
     */
    public Location getFrom() {
        return this.from;
    }

    /**
     * Sets the location to mark as where the player jumped from
     *
     * @param from New location to mark as the players previous location
     */
    public void setFrom(final Location from) {
        Preconditions.checkArgument(from != null, "Cannot use null from location!");
        Preconditions.checkArgument(from.getWorld() != null, "Cannot use from location with null world!");
        this.from = from.clone();
    }

    /**
     * Gets the location this player jumped to
     * <p>
     * This information is based on what the client sends, it typically
     * has little relation to the arc of the jump at any given point.
     *
     * @return Location the player jumped to
     */
    public Location getTo() {
        return this.to.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
