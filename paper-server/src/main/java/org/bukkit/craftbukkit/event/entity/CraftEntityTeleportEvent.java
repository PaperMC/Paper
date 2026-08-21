package org.bukkit.craftbukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityTeleportEvent extends CraftEntityEvent implements EntityTeleportEvent {

    private Location from;
    private Location to;

    private boolean cancelled;

    public CraftEntityTeleportEvent(final Entity entity, final Location from, final @Nullable Location to) {
        super(entity);
        this.from = from;
        this.to = to;
    }

    @Override
    public Location getFrom() {
        return this.from;
    }

    @Override
    public void setFrom(final Location from) {
        this.from = from.clone();
    }

    @Override
    public @Nullable Location getTo() {
        return this.to;
    }

    @Override
    public void setTo(final @Nullable Location to) {
        this.to = to != null ? to.clone() : null;
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
        return EntityTeleportEvent.getHandlerList();
    }
}
