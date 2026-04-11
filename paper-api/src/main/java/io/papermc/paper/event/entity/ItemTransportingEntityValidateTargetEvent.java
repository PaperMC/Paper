package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.CopperGolem;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an item-transporting entity (typically a {@link CopperGolem},
 * although other entities may be possible through non-API means)
 * is searching for or validating an existing target container block for
 * taking or depositing items.
 *
 * <p>This may be called multiple times per entity per tick, so listeners
 * should be careful to implement checks in an efficient manner.</p>
 */
@NullMarked
public class ItemTransportingEntityValidateTargetEvent extends EntityEvent {
    protected static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block block;
    private boolean allowed = true;

    @ApiStatus.Internal
    public ItemTransportingEntityValidateTargetEvent(
        final Entity entity,
        final Block block
    ) {
        super(entity);
        this.block = block;
    }

    /**
     * Sets if the entity is allowed to use {@link #getBlock()} as a target.
     *
     * @param allowed whether the target is allowed
     */
    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    /**
     * Gets if the entity is allowed to use {@link #getBlock()} as a target.
     *
     * @return true if the target is allowed
     */
    public boolean isAllowed() {
        return this.allowed;
    }

    /**
     * Gets the target block the entity is validating.
     *
     * @return the target block
     */
    public Block getBlock() {
        return this.block;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
