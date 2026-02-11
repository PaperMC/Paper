package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Called when a {@link LivingEntity} attempts to perform an automatic spin attack
 * against a target entity.
 */
public interface EntityAttemptSpinAttackEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Returns the entity that is being attacked
     * in the spin attack.
     *
     * @return the entity being attacked
     */
    LivingEntity getTarget();

    /**
     * {@inheritDoc}
     * <p>
     * It should be noted that both the client and server independently check
     * for a spin attack. Cancelling this on the server means the animation is not
     * interrupted and no attack is performed, but the client will still collide
     * and bounce away.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
