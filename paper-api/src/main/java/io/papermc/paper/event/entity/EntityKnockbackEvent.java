package io.papermc.paper.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.util.Vector;

/**
 * Called when an entity receives knockback.
 *
 * @see EntityPushedByEntityAttackEvent
 * @see com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
 */
public interface EntityKnockbackEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the cause of the knockback.
     *
     * @return the cause of the knockback
     */
    Cause getCause();

    /**
     * Gets the knockback force that will be applied to the entity. <br>
     * This value is read-only, changes made to it <b>will not</b> have any
     * effect on the final knockback received. Use {@link #setKnockback(Vector)}
     * to make changes.
     *
     * @return the knockback
     */
    Vector getKnockback();

    /**
     * Sets the knockback force that will be applied to the entity.
     *
     * @param knockback the knockback
     */
    void setKnockback(Vector knockback);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * An enum to specify the cause of the knockback.
     */
    enum Cause {

        /**
         * Knockback caused by non-entity damage.
         */
        DAMAGE,
        /**
         * Knockback caused by an attacking entity.
         */
        ENTITY_ATTACK,
        /**
         * Knockback caused by an explosion.
         */
        EXPLOSION,
        /**
         * Knockback caused by the target blocking with a shield.
         */
        SHIELD_BLOCK,
        /**
         * Knockback caused by a sweeping attack.
         */
        SWEEP_ATTACK,
        /**
         * A generic push.
         */
        PUSH,
        /**
         * Knockback with an unknown cause.
         */
        UNKNOWN
    }
}
