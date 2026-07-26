package org.bukkit.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a living entity receives knockback.
 *
 * @deprecated use {@link io.papermc.paper.event.entity.EntityKnockbackEvent}
 */
@Deprecated(forRemoval = true) // Paper
public class EntityKnockbackEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final KnockbackCause cause;
    private final double force;
    private final Vector rawKnockback;
    private Vector knockback;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityKnockbackEvent(@NotNull final LivingEntity entity, @NotNull final KnockbackCause cause, final double force, @NotNull final Vector rawKnockback, @NotNull final Vector knockback) {
        super(entity);

        this.cause = cause;
        this.force = force;
        this.rawKnockback = rawKnockback;
        this.knockback = knockback;
    }

    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /**
     * Gets the cause of the knockback.
     *
     * @return the cause of the knockback
     */
    @NotNull
    public KnockbackCause getCause() {
        return this.cause;
    }

    /**
     * Gets the raw force of the knockback.
     * <p>
     * This value is based on factors such as the {@link Enchantment#KNOCKBACK}
     * level of an attacker and the
     * {@link Attribute#KNOCKBACK_RESISTANCE} of the entity.
     *
     * @return the knockback force
     */
    public double getForce() {
        return this.force;
    }

    /**
     * Gets the raw knockback force that will be applied to the entity. <br>
     * This value is read-only, changes made to it <b>will not</b> have any
     * effect on the final knockback received.
     *
     * @return the raw knockback
     * @see #getFinalKnockback()
     */
    @NotNull
    public Vector getKnockback() {
        return this.rawKnockback.clone();
    }

    /**
     * Gets the force that will be applied to the entity. <br>
     * In contrast to {@link EntityKnockbackEvent#getKnockback()} this value is
     * affected by the entities current velocity and whether they are touching
     * the ground.
     * <p>
     * <b>Note:</b> this method returns a copy, changes must be applied with
     * {@link #setFinalKnockback(Vector)}.
     *
     * @return the final knockback
     */
    @NotNull
    public Vector getFinalKnockback() {
        return this.knockback.clone();
    }

    /**
     * Sets the force that will be applied to the entity.
     *
     * @param knockback the force to apply
     */
    @NotNull
    public void setFinalKnockback(@NotNull Vector knockback) {
        Preconditions.checkArgument(knockback != null, "Knockback cannot be null");

        this.knockback = knockback.clone();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * An enum to specify the cause of the knockback.
     */
    public enum KnockbackCause {

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
        UNKNOWN;
    }
}
