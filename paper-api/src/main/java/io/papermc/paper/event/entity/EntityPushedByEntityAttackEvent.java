package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * Fired when an entity is pushed by another entity's attack. The acceleration vector can be
 * modified. If this event is cancelled, the entity will not get pushed.
 * <p>
 * Note: Some entities might trigger this multiple times on the same entity
 * as multiple acceleration calculations are done.
 */
public interface EntityPushedByEntityAttackEvent extends EntityKnockbackEvent {

    /**
     * Gets the entity which pushed the affected entity.
     *
     * @return the pushing entity
     */
    Entity getPushedBy();

    /**
     * Gets the acceleration that will be applied to the affected entity.
     *
     * @return the acceleration vector
     * @deprecated use {@link #getKnockback()}
     */
    @Deprecated(since = "1.20.6", forRemoval = true)
    default Vector getAcceleration() {
        return this.getKnockback();
    }

    /**
     * Sets the relative acceleration that will be applied to the affected entity.
     *
     * @param acceleration the new acceleration vector
     * @deprecated use {@link #setKnockback(Vector)}
     */
    @Deprecated(since = "1.20.6", forRemoval = true)
    default void setAcceleration(final Vector acceleration) {
        this.setKnockback(acceleration);
    }
}
