package org.bukkit.event.entity;

import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an entity is about to be replaced by another entity.
 */
@NullMarked
public class EntityTransformEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<Entity> convertedList;
    private final Entity converted;
    private final TransformReason transformReason;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityTransformEvent(Entity original, List<Entity> convertedList, TransformReason transformReason) {
        super(original);
        this.convertedList = Collections.unmodifiableList(convertedList);
        this.converted = convertedList.getFirst();
        this.transformReason = transformReason;
    }

    /**
     * Gets the entities that the original entity was transformed to.
     *
     * @return The transformed entities.
     */
    public List<Entity> getTransformedEntities() {
        return this.convertedList;
    }

    /**
     * Gets the entity that the original entity was transformed to.
     * <br>
     * This returns the first entity in the transformed entity list.
     *
     * @return The transformed entity.
     * @see #getTransformedEntities()
     */
    public Entity getTransformedEntity() {
        return this.converted;
    }

    /**
     * Gets the reason for the conversion that has occurred.
     *
     * @return The reason for conversion that has occurred.
     */
    public TransformReason getTransformReason() {
        return this.transformReason;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum TransformReason {
        /**
         * When a zombie gets cured and a villager is spawned.
         */
        CURED,
        /**
         * When an entity is shaking in Powder Snow and a new entity spawns.
         */
        FROZEN,
        /**
         * When a villager gets infected and a zombie villager spawns.
         */
        INFECTION,
        /**
         * When an entity drowns in water and a new entity spawns.
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
        SPLIT,
        /**
         * When a piglin (or hoglin) converts to a zombified version from overworld presence.
         */
        PIGLIN_ZOMBIFIED,
        /**
         * When a tadpole converts to a frog
         */
        METAMORPHOSIS,
        /**
         * When reason is unknown.
         */
        UNKNOWN
    }
}
