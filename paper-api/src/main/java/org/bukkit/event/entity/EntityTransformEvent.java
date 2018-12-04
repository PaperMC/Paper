package org.bukkit.event.entity;

import java.util.Collections;
import java.util.List;
import org.bukkit.Warning;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity is about to be replaced by another entity.
 *
 * @deprecated draft API
 */
@Deprecated
@Warning(false)
public class EntityTransformEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Entity converted;
    private final List<Entity> convertedList;
    private final TransformReason transformReason;

    public EntityTransformEvent(Entity original, List<Entity> convertedList, TransformReason transformReason) {
        super(original);
        this.convertedList = Collections.unmodifiableList(convertedList);
        this.converted = convertedList.get(0);
        this.transformReason = transformReason;
    }

    /**
     * Gets the entity that the original entity was transformed to.
     *
     * This returns the first entity in the transformed entity list.
     *
     * @return The transformed entity.
     * @see #getTransformedEntities()
     */
    public Entity getTransformedEntity() {
        return converted;
    }

    /**
     * Gets the entities that the original entity was transformed to.
     *
     * @return The transformed entities.
     */
    public List<Entity> getTransformedEntities() {
        return convertedList;
    }

    /**
     * Gets the reason for the conversion that has occurred.
     *
     * @return The reason for conversion that has occurred.
     */
    public TransformReason getTransformReason() {
        return transformReason;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum TransformReason {
        /**
         * When a zombie gets cured and a villager is spawned.
         */
        CURED,
        /**
         * When a villager gets infected and a zombie villager spawns.
         */
        INFECTION,
        /**
         * When a entity drowns in water and a new entity spawns.
         */
        DROWNED,
        /**
         * When a mooshroom (or MUSHROOM_COW) is sheared and a cow spawns.
         */
        SHEARED,
        /**
         * When lightning strikes a entity.
         */
        LIGHTNING,
        /**
         * When a slime splits into multiple smaller slimes.
         */
        SPLIT
    }
}
