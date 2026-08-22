package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.AbstractCubeMob;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.SlimeSplitEvent;

public class CraftSlimeSplitEvent extends CraftEntityEvent implements SlimeSplitEvent {

    private int count;
    private boolean cancelled;

    public CraftSlimeSplitEvent(final AbstractCubeMob cubeMob, final int count) {
        super(cubeMob);
        this.count = count;
    }

    @Override
    public AbstractCubeMob getEntity() {
        return (AbstractCubeMob) this.entity;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public void setCount(final int count) {
        this.count = count;
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
        return SlimeSplitEvent.getHandlerList();
    }
}
