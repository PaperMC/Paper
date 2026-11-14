package io.papermc.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an entity is launched upward by a Potent Sulfur geyser.
 */
@NullMarked
public class EntityPotentSulfurLaunchEvent extends EntityEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block sulfurBlock;
    private final Location sourceLocation;

    private final Vector launchVelocity;

    @ApiStatus.Internal
    public EntityPotentSulfurLaunchEvent(
        final Entity entity,
        final Block sulfurBlock,
        final Location sourceLocation,
        final Vector launchVelocity
    ) {
        super(entity);
        this.sulfurBlock = sulfurBlock;
        this.sourceLocation = sourceLocation;
        this.launchVelocity = launchVelocity;
    }

    /**
     * Gets the Potent Sulfur block.
     */
    public Block getSulfurBlock() {
        return this.sulfurBlock;
    }

    /**
     * Gets the location where the geyser exits the water column.
     */
    public Location getSourceLocation() {
        return this.sourceLocation.clone();
    }

    /**
     * Gets the velocity that will be added to the entity.
     * <br>
     * This value is read-only, changes made to it <b>will not</b> have any
     * effect on the final launch velocity.
     */
    public Vector getLaunchVelocity() {
        return this.launchVelocity.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
