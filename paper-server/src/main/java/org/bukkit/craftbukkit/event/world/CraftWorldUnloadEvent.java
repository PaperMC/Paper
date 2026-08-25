package org.bukkit.craftbukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldUnloadEvent;

public class CraftWorldUnloadEvent extends CraftWorldEvent implements WorldUnloadEvent {

    private boolean cancelled;

    public CraftWorldUnloadEvent(final World world) {
        super(world);
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
        return WorldUnloadEvent.getHandlerList();
    }
}
