package org.bukkit.craftbukkit.event.hanging;

import org.bukkit.entity.Hanging;
import org.bukkit.event.HandlerList;
import org.bukkit.event.hanging.HangingBreakEvent;

public class CraftHangingBreakEvent extends CraftHangingEvent implements HangingBreakEvent {

    private final CraftHangingBreakEvent.RemoveCause cause;
    private boolean cancelled;

    public CraftHangingBreakEvent(final Hanging hanging, final RemoveCause cause) {
        super(hanging);
        this.cause = cause;
    }

    @Override
    public RemoveCause getCause() {
        return this.cause;
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
        return HangingBreakEvent.getHandlerList();
    }
}
