package io.papermc.paper.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Runs when a player attempts to move a vehicle to a new position.
 */
@NullMarked
public class PlayerMoveVehicleEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location from;
    private final Location to;
    private final Entity vehicle;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerMoveVehicleEvent(final Player player, final Entity vehicle, final Location from, final Location to) {
        super(player);
        this.vehicle = vehicle;
        this.from = from;
        this.to = to;
    }
    /**
     * Gets the location the vehicle moved from
     *
     * @return Location the vehicle moved from
     */
    public Location getFrom() {
        return this.from.clone();
    }

    /**
     * Gets the location the vehicle is moving to
     *
     * @return Location the vehicle is moving to
     */
    public Location getTo() {
        return this.to.clone();
    }

    /**
     * Gets the vehicle this player is currently moving.
     * Note: You should use {@link PlayerMoveVehicleEvent#getTo()} for getting the previous and
     * new location of the vehicle.
     * <p>
     * The behavior of getting the location of the vehicle in this event is undefined.
     *
     * @return vehicle
     */
    public Entity getVehicle() {
        return vehicle;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
