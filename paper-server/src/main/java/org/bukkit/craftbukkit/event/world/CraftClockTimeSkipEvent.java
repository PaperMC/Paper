package org.bukkit.craftbukkit.event.world;

import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ClockTimeSkipEvent;
import org.jetbrains.annotations.ApiStatus;

// TODO - snapshot - 26.1 clock
@ApiStatus.Experimental
public class CraftClockTimeSkipEvent extends CraftEvent implements ClockTimeSkipEvent {

    private final SkipReason skipReason;
    private long skipAmount;

    private boolean cancelled;

    public CraftClockTimeSkipEvent(final SkipReason skipReason, final long skipAmount) {
        this.skipReason = skipReason;
        this.skipAmount = skipAmount;
    }

    @Override
    public SkipReason getSkipReason() {
        return this.skipReason;
    }

    @Override
    public long getSkipAmount() {
        return this.skipAmount;
    }

    @Override
    public void setSkipAmount(final long skipAmount) {
        this.skipAmount = skipAmount;
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
        return ClockTimeSkipEvent.getHandlerList();
    }
}
