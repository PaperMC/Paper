package org.bukkit.craftbukkit.event.hanging;

import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.event.HandlerList;
import org.bukkit.event.hanging.HangingBreakEvent;

public class CraftHangingBreakEvent extends CraftHangingEvent implements HangingBreakEvent {

    private final RemoveCause cause;
    private boolean cancelled;

    public CraftHangingBreakEvent(final Hanging hanging, final RemoveCause cause) {
        super(hanging);
        this.cause = cause;
    }

    public CraftHangingBreakEvent(final Entity hanging, final RemoveCause cause) {
        this((Hanging) hanging.getBukkitEntity(), cause);
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
