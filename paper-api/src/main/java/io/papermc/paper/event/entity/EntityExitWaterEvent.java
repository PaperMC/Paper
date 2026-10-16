package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when an {@link Entity} stops touching water. This includes even small amounts of flowing water.
 *
 * @see EntityTouchWaterEvent for when an {@link Entity} starts touching the water.
 */
@NullMarked
public class EntityExitWaterEvent extends EntityEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public EntityExitWaterEvent(final Entity entity) {
        super(entity);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
