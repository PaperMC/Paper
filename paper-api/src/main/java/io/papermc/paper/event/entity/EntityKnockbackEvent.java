package io.papermc.paper.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an entity receives knockback.
 * @see EntityPushedByEntityAttackEvent
 * @see com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
 */
@NullMarked
public class EntityKnockbackEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Cause cause;
    protected Vector knockback;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityKnockbackEvent(final Entity entity, final EntityKnockbackEvent.Cause cause, final Vector knockback) {
        super(entity);
        this.cause = cause;
        this.knockback = knockback;
    }

    /**
     * Gets the cause of the knockback.
     *
     * @return the cause of the knockback
     */
    public EntityKnockbackEvent.Cause getCause() {
        return this.cause;
    }

    /**
     * Gets the knockback force that will be applied to the entity. <br>
     * This value is read-only, changes made to it <b>will not</b> have any
     * effect on the final knockback received. Use {@link #setKnockback(Vector)}
     * to make changes.
     *
     * @return the knockback
     */
    public Vector getKnockback() {
        return this.knockback.clone();
    }

    /**
     * Sets the knockback force that will be applied to the entity.
     *
     * @param knockback the knockback
     */
    public void setKnockback(final Vector knockback) {
        Preconditions.checkArgument(knockback != null, "knockback");
        this.knockback = knockback.clone();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * An enum to specify the cause of the knockback.
     */
    public enum Cause {

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
