package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@SuppressWarnings("serial")
/**
 * Called when a LivingEntity changes a block
 *
 * This event specifically excludes player entities
 */
public class EntityChangeBlockEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Block block;
    private boolean cancel;
    private Material to;

    public EntityChangeBlockEvent(Entity what, Block block, Material to) {
        super(Type.ENTITY_CHANGE_BLOCK, what);
        this.block = block;
        this.cancel = false;
        this.to = to;
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
