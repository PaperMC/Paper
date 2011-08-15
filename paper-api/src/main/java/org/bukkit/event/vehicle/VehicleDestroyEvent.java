package org.bukkit.event.vehicle;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Raised when a vehicle is destroyed, which could be caused by either a player
 * or the environment. This is not raised if the boat is simply 'removed'
 * due to other means.
 */
public class VehicleDestroyEvent extends VehicleEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity attacker;
    private boolean cancelled;
    private List<ItemStack> drops;

    public VehicleDestroyEvent(final Vehicle vehicle, final Entity attacker, List<ItemStack> drops) {
        super(vehicle);
        this.attacker = attacker;
        this.drops = drops;
    }

    /**
     * Gets the Entity that has destroyed the vehicle, potentially null
     *
     * @return the Entity that has destroyed the vehicle, potentially null
     */
    public Entity getAttacker() {
        return attacker;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets a list of drops that this vehicle should drop when broken. Changes to this list will
     * affect what is actually dropped. This list does not include the contents of the inventory
     * if this is a storage minecart; if that's needed, it can be fetched by casting.
     * @return A list of drops
     */
    public List<ItemStack> getDrops() {
        return drops;
    }
}
