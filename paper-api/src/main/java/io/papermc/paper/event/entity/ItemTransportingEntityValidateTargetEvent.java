package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.CopperGolem;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Called when an item-transporting entity (typically a {@link CopperGolem},
 * although other entities may be possible through non-API means)
 * is searching for or validating an existing target container block for
 * taking or depositing items.
 * <p>
 * This may be called multiple times per entity per tick, so listeners
 * should be careful to implement checks in an efficient manner.
 */
public interface ItemTransportingEntityValidateTargetEvent extends EntityEventNew {

    /**
     * Gets the target block the entity is validating.
     *
     * @return the target block
     */
    Block getBlock();

    /**
     * Gets if the entity is allowed to use {@link #getBlock()} as a target.
     *
     * @return true if the target is allowed
     */
    boolean isAllowed();

    /**
     * Sets if the entity is allowed to use {@link #getBlock()} as a target.
     *
     * @param allowed whether the target is allowed
     */
    void setAllowed(boolean allowed);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
