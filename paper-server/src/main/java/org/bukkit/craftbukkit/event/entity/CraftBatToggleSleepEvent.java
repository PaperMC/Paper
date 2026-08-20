package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Bat;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.BatToggleSleepEvent;

public class CraftBatToggleSleepEvent extends CraftEntityEvent implements BatToggleSleepEvent {

    private final boolean awake;
    private boolean cancelled;

    public CraftBatToggleSleepEvent(final Bat bat, final boolean awake) {
        super(bat);
        this.awake = awake;
    }

    @Override
    public boolean isAwake() {
        return this.awake;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return BatToggleSleepEvent.getHandlerList();
    }
}
