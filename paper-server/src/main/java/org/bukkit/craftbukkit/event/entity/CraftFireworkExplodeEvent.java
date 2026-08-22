package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Firework;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.FireworkExplodeEvent;

public class CraftFireworkExplodeEvent extends CraftEntityEvent implements FireworkExplodeEvent {

    private boolean cancelled;

    public CraftFireworkExplodeEvent(final Firework firework) {
        super(firework);
    }

    @Override
    public Firework getEntity() {
        return (Firework) this.entity;
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
        return FireworkExplodeEvent.getHandlerList();
    }
}
