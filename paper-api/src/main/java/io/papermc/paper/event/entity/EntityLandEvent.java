package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an {@link Entity} lands on the block after falling.
 * This event is called before {@link org.bukkit.event.entity.EntityDamageEvent}, {@link org.bukkit.event.entity.EntityChangeBlockEvent}, {@link org.bukkit.event.player.PlayerInteractEvent}
 * and {@link org.bukkit.event.entity.EntityInteractEvent}.
 */
@NullMarked
public class EntityLandEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block block;
    private boolean cancelled;

    public EntityLandEvent(final Entity entity, final Block block) {
        super(entity);
        this.block = block;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Returns the block that the entity landed on.
     *
     * @return the block that the entity landed on
     */
    public Block getBlock() {
        return this.block;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets whether to cancel the landing, cancelling this event will prevent
     * the entity from taking fall damage and interact with the block. (ex: break turtle egg)
     *
     * @param cancel true if the event should be cancelled, false otherwise
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
