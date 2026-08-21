package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityExhaustionEvent;

public class CraftEntityExhaustionEvent extends CraftEntityEvent implements EntityExhaustionEvent {

    private final ExhaustionReason exhaustionReason;
    private float exhaustion;

    private boolean cancelled;

    public CraftEntityExhaustionEvent(final HumanEntity human, final ExhaustionReason exhaustionReason, final float exhaustion) {
        super(human);
        this.exhaustionReason = exhaustionReason;
        this.exhaustion = exhaustion;
    }

    @Override
    public HumanEntity getEntity() {
        return (HumanEntity) this.entity;
    }

    @Override
    public ExhaustionReason getExhaustionReason() {
        return this.exhaustionReason;
    }

    @Override
    public float getExhaustion() {
        return this.exhaustion;
    }

    @Override
    public void setExhaustion(final float exhaustion) {
        this.exhaustion = exhaustion;
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
        return EntityExhaustionEvent.getHandlerList();
    }
}
