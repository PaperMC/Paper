package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a LivingEntity changes a block
 *
 * This event specifically excludes player entities
 */
public class EntityChangeBlockEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private boolean cancel;
    private final Material to;
    private final byte data;

    public EntityChangeBlockEvent(final LivingEntity what, final Block block, final Material to) {
        this (what, block, to, (byte) 0);
    }

    public EntityChangeBlockEvent(final LivingEntity what, final Block block, final Material to, final byte data) {
        super(what);
        this.block = block;
        this.cancel = false;
        this.to = to;
        this.data = data;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }

    /**
     * Gets the block the entity is changing
     *
     * @return the block that is changing
     */
    public Block getBlock() {
        return block;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the Material that the block is changing into
     *
     * @return the material that the block is changing into
     */
    public Material getTo() {
        return to;
    }

    /**
     * Gets the data for the block that would be changed into
     *
     * @return the data for the block that would be changed into
     */
    public byte getData() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
