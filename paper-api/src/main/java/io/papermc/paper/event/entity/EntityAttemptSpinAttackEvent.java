package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a {@link LivingEntity} attempts to perform an automatic spin attack
 * against a target entity.
 */
@NullMarked
public class EntityAttemptSpinAttackEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LivingEntity target;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityAttemptSpinAttackEvent(final LivingEntity entity, final LivingEntity target) {
        super(entity);
        this.target = target;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /**
     * Returns the entity that is being attacked
     * in the spin attack.
     *
     * @return the entity being attacked
     */
    public LivingEntity getTarget() {
        return this.target;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It should be noted that both the client and server independently check
     * for a spin attack. Cancelling this on the server means the animation is not
     * interrupted and no attack is performed, but the client will still collide
     * and bounce away.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
