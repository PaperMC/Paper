package org.bukkit.event.entity;

import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

/**
 * Called when a living entity receives knockback.
 *
 * @deprecated use {@link io.papermc.paper.event.entity.EntityKnockbackEvent}
 */
@Deprecated(forRemoval = true)
public interface EntityKnockbackEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Gets the cause of the knockback.
     *
     * @return the cause of the knockback
     */
    KnockbackCause getCause();

    /**
     * Gets the raw force of the knockback.
     * <p>
     * This value is based on factors such as the {@link Enchantment#KNOCKBACK}
     * level of an attacker and the
     * {@link Attribute#KNOCKBACK_RESISTANCE} of the entity.
     *
     * @return the knockback force
     */
    double getForce();

    /**
     * Gets the raw knockback force that will be applied to the entity.
     * <p>
     * This value is read-only, changes made to it <b>will not</b> have any
     * effect on the final knockback received.
     *
     * @return the raw knockback
     * @see #getFinalKnockback()
     */
    Vector getKnockback();

    /**
     * Gets the force that will be applied to the entity.
     * <p>
     * In contrast to {@link EntityKnockbackEvent#getKnockback()} this value is
     * affected by the entities current velocity and whether they are touching
     * the ground.
     * <p>
     * <b>Note:</b> this method returns a copy, changes must be applied with
     * {@link #setFinalKnockback(Vector)}.
     *
     * @return the final knockback
     */
    Vector getFinalKnockback();

    /**
     * Sets the force that will be applied to the entity.
     *
     * @param knockback the force to apply
     */
    void setFinalKnockback(Vector knockback);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * An enum to specify the cause of the knockback.
     */
    enum KnockbackCause {

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
         * Knockback with an unknown cause.
         */
        UNKNOWN
    }
}
