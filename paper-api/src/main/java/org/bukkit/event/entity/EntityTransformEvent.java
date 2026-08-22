package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity is about to be replaced by another entity.
 */
public interface EntityTransformEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the entities that the original entity was transformed to.
     *
     * @return The transformed entities.
     */
    List<Entity> getTransformedEntities();

    /**
     * Gets the entity that the original entity was transformed to.
     * <br>
     * This returns the first entity in the transformed entity list.
     *
     * @return The transformed entity.
     * @see #getTransformedEntities()
     */
    Entity getTransformedEntity();

    /**
     * Gets the reason for the conversion that has occurred.
     *
     * @return The reason for conversion that has occurred.
     */
    TransformReason getTransformReason();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum TransformReason {
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
