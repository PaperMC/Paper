package io.papermc.paper.event.entity;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an entity switches to another world.
 * <p>
 * Not called for players, use {@link org.bukkit.event.player.PlayerChangedWorldEvent} to handle player world switches.
 */
@NullMarked
public final class EntityChangeWorldEvent extends EntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final World from;

    @ApiStatus.Internal
    public EntityChangeWorldEvent(final Entity entity, final World from) {
        super(entity);
        this.from = from;
    }

    /**
     * Gets the world that the entity switched from.
     *
     * @return the previous world of the entity
     */
    public World getFrom() {
        return from;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
