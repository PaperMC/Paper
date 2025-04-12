package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity attempts to ride another entity.
 */
public class EntityMountEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity mount;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityMountEvent(@NotNull Entity entity, @NotNull Entity mount) {
        super(entity);
        this.mount = mount;
    }

    /**
     * Gets the entity which will be ridden.
     *
     * @return mounted entity
     */
    @NotNull
    public Entity getMount() {
        return this.mount;
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
