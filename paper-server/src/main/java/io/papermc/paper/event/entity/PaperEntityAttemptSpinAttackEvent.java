package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

public class PaperEntityAttemptSpinAttackEvent extends CraftEntityEvent implements EntityAttemptSpinAttackEvent {

    private final LivingEntity target;
    private boolean cancelled;

    public PaperEntityAttemptSpinAttackEvent(final LivingEntity entity, final LivingEntity target) {
        super(entity);
        this.target = target;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public LivingEntity getTarget() {
        return this.target;
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
        return EntityAttemptSpinAttackEvent.getHandlerList();
    }
}
