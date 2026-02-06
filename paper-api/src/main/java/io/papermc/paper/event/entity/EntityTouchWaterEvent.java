package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Event when an Entity starts touching water or not. This includes even small amounts of flowing water.
 */
@NullMarked
public class EntityTouchWaterEvent extends EntityEvent{

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean inWater;

    @ApiStatus.Internal
    public EntityTouchWaterEvent(final Entity entity, final boolean inWater) {
        super(entity);
        this.inWater = inWater;
    }

    /**
     * @return If the entity is now touching water.
     */
    public boolean isInWater() {
        return inWater;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
