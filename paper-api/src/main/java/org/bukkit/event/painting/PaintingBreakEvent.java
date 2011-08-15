package org.bukkit.event.painting;

import java.util.List;

import org.bukkit.entity.Painting;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Triggered when a painting is removed
 */
public class PaintingBreakEvent extends PaintingEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final RemoveCause cause;
    private List<ItemStack> drops;

    public PaintingBreakEvent(final Painting painting, final RemoveCause cause, List<ItemStack> drops) {
        super(painting);
        this.cause = cause;
        this.drops = drops;
    }

    /**
     * Gets the cause for the painting's removal
     *
     * @return the RemoveCause for the painting's removal
     */
    public RemoveCause getCause() {
        return cause;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the list of items to be dropped. Modifying this list will modify what's actually dropped.
     * @return A list of drops
     */
    public List<ItemStack> getDrops() {
        return drops;
    }

    /**
     * An enum to specify the cause of the removal
     */
    public enum RemoveCause {
        /**
         * Removed by an entity
         */
        ENTITY,
        /**
         * Removed by fire
         */
        FIRE,
        /**
         * Removed by placing a block on it
         */
        OBSTRUCTION,
        /**
         * Removed by water flowing over it
         */
        WATER,
        /**
         * Removed by destroying the block behind it, etc
         */
        PHYSICS,
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
