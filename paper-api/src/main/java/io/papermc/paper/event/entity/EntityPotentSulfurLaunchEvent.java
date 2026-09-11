package io.papermc.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an entity is launched upward by a Potent Sulfur geyser.
 */
@NullMarked
public class EntityPotentSulfurLaunchEvent extends EntityEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location sulfurLocation;
    private final Location sourceLocation;

    private Vector launchVelocity;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityPotentSulfurLaunchEvent(
        Entity entity,
        Location sulfurLocation,
        Location sourceLocation,
        Vector launchVelocity
    ) {
        super(entity);
        this.sulfurLocation = sulfurLocation;
        this.sourceLocation = sourceLocation;
        this.launchVelocity = launchVelocity;
    }

    /**
     * Gets the location of the Potent Sulfur block.
     */
    public Location getSulfurLocation() {
        return this.sulfurLocation.clone();
    }

    /**
     * Gets the location where the geyser exits the water column.
     */
    public Location getSourceLocation() {
        return this.sourceLocation.clone();
    }

    /**
     * Gets the velocity that will be added to the entity.
     */
    public Vector getLaunchVelocity() {
        return this.launchVelocity.clone();
    }

    /**
     * Changes the velocity that will be added to the entity.
     */
    public void setLaunchVelocity(Vector launchVelocity) {
        this.launchVelocity = launchVelocity.clone();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
