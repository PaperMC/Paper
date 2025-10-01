package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.CopperGolem;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a {@link CopperGolem} is searching for or validating an existing
 * target container block for taking or depositing items.
 *
 * <p>This may be called multiple times per golem per tick, so listeners
 * should be careful to implement checks in an efficient manner.</p>
 */
@NullMarked
public class CopperGolemValidateTargetEvent extends EntityEvent {
    public static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block block;
    private boolean allowed = true;

    @ApiStatus.Internal
    public CopperGolemValidateTargetEvent(
        final CopperGolem copperGolem,
        final Block block
    ) {
        super(copperGolem);
        this.block = block;
    }

    /**
     * Sets if the {@link CopperGolem} is allowed to use {@link #getBlock()} as a target.
     *
     * @param allowed whether the target is allowed
     */
    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    /**
     * Gets if the {@link CopperGolem} is allowed to use {@link #getBlock()} as a target.
     *
     * @return true if the target is allowed
     */
    public boolean isAllowed() {
        return this.allowed;
    }

    @Override
    public CopperGolem getEntity() {
        return (CopperGolem) super.getEntity();
    }

    /**
     * Gets the target block the {@link CopperGolem} is validating.
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
