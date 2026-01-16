package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when an entity is pushed by another entity's attack. The acceleration vector can be
 * modified. If this event is cancelled, the entity will not get pushed.
 * <p>
 * Note: Some entities might trigger this multiple times on the same entity
 * as multiple acceleration calculations are done.
 */
@NullMarked
public class EntityPushedByEntityAttackEvent extends EntityKnockbackEvent {

    private final Entity pushedBy;

    @ApiStatus.Internal
    public EntityPushedByEntityAttackEvent(final Entity entity, final EntityKnockbackEvent.Cause cause, final Entity pushedBy, final Vector knockback) {
        super(entity, cause, knockback);
        this.pushedBy = pushedBy;
    }

    /**
     * Gets the entity which pushed the affected entity.
     *
     * @return the pushing entity
     */
    public Entity getPushedBy() {
        return this.pushedBy;
    }

    /**
     * Gets the acceleration that will be applied to the affected entity.
     *
     * @return the acceleration vector
     * @deprecated use {@link #getKnockback()}
     */
    @Deprecated(since = "1.20.6", forRemoval = true)
    public Vector getAcceleration() {
        return this.knockback; // TODO Clone in 1.21 to not instantly break what was technically already modifiable (call super.getKnockback())
    }

    /**
     * Sets the relative acceleration that will be applied to the affected entity.
     *
     * @param acceleration the new acceleration vector
     * @deprecated use {@link #setKnockback(Vector)}
     */
    @Deprecated(since = "1.20.6", forRemoval = true)
    public void setAcceleration(final Vector acceleration) {
        super.setKnockback(acceleration);
    }
}
