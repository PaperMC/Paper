package org.bukkit.craftbukkit.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;

public class CraftArrowBodyCountChangeEvent extends CraftEntityEvent implements ArrowBodyCountChangeEvent {

    private final boolean reset;
    private final int oldAmount;
    private int newAmount;

    private boolean cancelled;

    public CraftArrowBodyCountChangeEvent(final LivingEntity entity, final int oldAmount, final int newAmount, final boolean reset) {
        super(entity);

        this.oldAmount = oldAmount;
        this.newAmount = newAmount;
        this.reset = reset;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public boolean isReset() {
        return this.reset;
    }

    @Override
    public int getOldAmount() {
        return this.oldAmount;
    }

    @Override
    public int getNewAmount() {
        return this.newAmount;
    }

    @Override
    public void setNewAmount(final int newAmount) {
        Preconditions.checkArgument(newAmount >= 0, "New arrow amount must be >= 0");
        this.newAmount = newAmount;
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
        return ArrowBodyCountChangeEvent.getHandlerList();
    }
}
