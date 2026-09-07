package io.papermc.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is launched upward by a Potent Sulfur geyser.
 */
public class EntityPotentSulfurLaunchEvent extends EntityEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location sulfurLocation;
    private final Location sourceLocation;

    private Vector launchVelocity;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityPotentSulfurLaunchEvent(@NotNull Entity entity, @NotNull Location sulfurLocation, @NotNull Location sourceLocation, @NotNull Vector launchVelocity) {
        super(entity);
        this.sulfurLocation = sulfurLocation;
        this.sourceLocation = sourceLocation;
        this.launchVelocity = launchVelocity;
    }

    /**
     * Gets the location of the Potent Sulfur block.
     */
    public @NotNull Location getSulfurLocation() {
        return this.sulfurLocation.clone();
    }

    /**
     * Gets the location where the geyser exits the water column.
     */
    public @NotNull Location getSourceLocation() {
        return this.sourceLocation.clone();
    }

    /**
     * Gets the velocity that will be added to the entity.
     */
    public @NotNull Vector getLaunchVelocity() {
        return this.launchVelocity.clone();
    }

    /**
     * Changes the velocity that will be added to the entity.
     */
    public void setLaunchVelocity(@NotNull Vector launchVelocity) {
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
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
