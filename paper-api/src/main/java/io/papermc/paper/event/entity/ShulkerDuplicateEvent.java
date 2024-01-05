package io.papermc.paper.event.entity;

import org.bukkit.entity.Shulker;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a shulker duplicates itself by spawning a new shulker.
 * <p>
 * The event is fired prior to the newly created shulker, accessible via {@link #getEntity()}, being added to the world.
 */
@NullMarked
public class ShulkerDuplicateEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Shulker parent;
    private boolean cancelled;

    @ApiStatus.Internal
    public ShulkerDuplicateEvent(final Shulker child, final Shulker parent) {
        super(child);
        this.parent = parent;
    }

    /**
     * Provides the newly created shulker, which did not exist prior to the duplication.
     * At the point of this event, said shulker is not part of the world yet.
     *
     * @return the newly duplicated shulker.
     */
    @Override
    public Shulker getEntity() {
        return (Shulker) super.getEntity();
    }

    /**
     * Provides the "parent" of the freshly created shulker.
     * The parent shulker is the one that initiated the duplication.
     *
     * @return the previously existing shulker which duplicated.
     */
    public Shulker getParent() {
        return this.parent;
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
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
