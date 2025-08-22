package org.bukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link LivingEntity} attempts to perform an automatic spin attack
 * against a target entity.
 */
public class EntityAttemptSpinAttackEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LivingEntity target;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityAttemptSpinAttackEvent(@NotNull LivingEntity entity, @NotNull LivingEntity target) {
        super(entity);
        this.target = target;
    }

    @Override
    @NotNull
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /**
     * Returns the entity that is being attacked
     * in the spin attack.
     *
     * @return the entity being attacked
     */
    @NotNull
    public LivingEntity getTarget() {
        return this.target;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
