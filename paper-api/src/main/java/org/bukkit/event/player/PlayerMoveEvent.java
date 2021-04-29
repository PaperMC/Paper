package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Holds information for player movement events
 */
public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private Location from;
    private Location to;

    public PlayerMoveEvent(@NotNull final Player player, @NotNull final Location from, @Nullable final Location to) {
        super(player);
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     * <p>
     * If a move or teleport event is cancelled, the player will be moved or
     * teleported back to the Location as defined by getFrom(). This will not
     * fire an event
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     * <p>
     * If a move or teleport event is cancelled, the player will be moved or
     * teleported back to the Location as defined by getFrom(). This will not
     * fire an event
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the location this player moved from
     *
     * @return Location the player moved from
     */
    @NotNull
    public Location getFrom() {
        return from;
    }

    /**
     * Sets the location to mark as where the player moved from
     *
     * @param from New location to mark as the players previous location
     */
    public void setFrom(@NotNull Location from) {
        validateLocation(from);
        this.from = from;
    }

    /**
     * Gets the location this player moved to
     *
     * @return Location the player moved to
     */
    @NotNull // Paper
    public Location getTo() {
        return to;
    }

    /**
     * Sets the location that this player will move to
     *
     * @param to New Location this player will move to
     */
    public void setTo(@NotNull Location to) {
        validateLocation(to);
        this.to = to;
    }

    // Paper start - PlayerMoveEvent improvements
    /**
     * Check if the player has changed position (even within the same block) in the event
     *
     * @return whether the player has changed position or not
     */
    public boolean hasChangedPosition() {
        return hasExplicitlyChangedPosition() || !from.getWorld().equals(to.getWorld());
    }

    /**
     * Check if the player has changed position (even within the same block) in the event, disregarding a possible world change
     *
     * @return whether the player has changed position or not
     */
    public boolean hasExplicitlyChangedPosition() {
        return from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ();
    }

    /**
     * Check if the player has moved to a new block in the event
     *
     * @return whether the player has moved to a new block or not
     */
    public boolean hasChangedBlock() {
        return hasExplicitlyChangedBlock() || !from.getWorld().equals(to.getWorld());
    }

    /**
     * Check if the player has moved to a new block in the event, disregarding a possible world change
     *
     * @return whether the player has moved to a new block or not
     */
    public boolean hasExplicitlyChangedBlock() {
        return from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
    }

    /**
     * Check if the player has changed orientation in the event
     *
     * @return whether the player has changed orientation or not
     */
    public boolean hasChangedOrientation() {
        return from.getPitch() != to.getPitch() || from.getYaw() != to.getYaw();
    }
    // Paper end

    private void validateLocation(@NotNull Location loc) {
        Preconditions.checkArgument(loc != null, "Cannot use null location!");
        Preconditions.checkArgument(loc.getWorld() != null, "Cannot use null location with null world!");
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
