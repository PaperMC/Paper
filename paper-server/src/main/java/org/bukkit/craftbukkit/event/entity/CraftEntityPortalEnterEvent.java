package org.bukkit.craftbukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.PortalType;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPortalEnterEvent;

public class CraftEntityPortalEnterEvent extends CraftEntityEvent implements EntityPortalEnterEvent {

    private final Location location;
    private final PortalType portalType;

    private boolean cancelled;

    public CraftEntityPortalEnterEvent(final Entity entity, final Location location, final PortalType portalType) {
        super(entity);
        this.location = location;
        this.portalType = portalType;
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public PortalType getPortalType() {
        return this.portalType;
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
        return EntityPortalEnterEvent.getHandlerList();
    }
}
