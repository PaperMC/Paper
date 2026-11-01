package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a registered entity moves to another chunk section.
 */
@NullMarked
public final class EntityChangeChunkSectionEvent extends EntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int previousX;
    private final int previousY;
    private final int previousZ;

    @ApiStatus.Internal
    public EntityChangeChunkSectionEvent(Entity entity, int previousX, int previousY, int previousZ) {
        super(entity);
        this.previousX = previousX;
        this.previousY = previousY;
        this.previousZ = previousZ;
    }

    /**
     * Gets the {@code X} coordinate of the previous chunk section.
     *
     * @return the {@code X} coordinate
     */
    public int getPreviousX() {
        return this.previousX;
    }

    /**
     * Gets the {@code Y} coordinate of the previous chunk section.
     *
     * @return the {@code Y} coordinate
     */
    public int getPreviousY() {
        return this.previousY;
    }

    /**
     * Gets the {@code Z} coordinate of the previous chunk section.
     *
     * @return the {@code Z} coordinate
     */
    public int getPreviousZ() {
        return this.previousZ;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
