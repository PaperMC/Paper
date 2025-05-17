package io.papermc.paper.event.entity;

import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an item attempts to merge with another item.
 * <p>
 * In vanilla, the item only tries to merge with another item
 * if it's stack size is less than it's maximum stack size.
 */
@NullMarked
public class ItemAttemptMergeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Item target;
    private boolean cancelled;

    @ApiStatus.Internal
    public ItemAttemptMergeEvent(final Item entity, final Item target) {
        super(entity);
        this.target = target;
    }

    @Override
    public Item getEntity() {
        return (Item) super.getEntity();
    }

    /**
     * Gets the item entity the main item is being merged into.
     *
     * @return The item being merged
     */
    public Item getTarget() {
        return this.target;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
