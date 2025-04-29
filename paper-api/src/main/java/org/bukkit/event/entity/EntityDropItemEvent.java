package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when an entity creates an item drop.
 */
public class EntityDropItemEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Item drop;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityDropItemEvent(@NotNull final Entity entity, @NotNull final Item drop) {
        super(entity);
        this.drop = drop;
    }

    /**
     * Gets the Item created by the entity
     *
     * @return Item created by the entity
     */
    @NotNull
    public Item getItemDrop() {
        return this.drop;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
