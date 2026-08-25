package org.bukkit.craftbukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.PortalType;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPortalEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityPortalEvent extends CraftEntityTeleportEvent implements EntityPortalEvent {

    private final PortalType type;
    private int searchRadius;
    private boolean canCreatePortal;
    private int creationRadius;

    public CraftEntityPortalEvent(final Entity entity, final Location from, final @Nullable Location to, final int searchRadius, final boolean canCreatePortal, final int creationRadius, final PortalType portalType) {
        super(entity, from, to);
        this.type = portalType;
        this.searchRadius = searchRadius;
        this.canCreatePortal = canCreatePortal;
        this.creationRadius = creationRadius;
    }

    @Override
    public PortalType getPortalType() {
        return this.type;
    }

    @Override
    public int getSearchRadius() {
        return this.searchRadius;
    }

    @Override
    public void setSearchRadius(final int searchRadius) {
        this.searchRadius = searchRadius;
    }

    @Override
    public boolean canCreatePortal() {
        return this.canCreatePortal;
    }

    @Override
    public void setCanCreatePortal(final boolean canCreatePortal) {
        this.canCreatePortal = canCreatePortal;
    }

    @Override
    public int getCreationRadius() {
        return this.creationRadius;
    }

    @Override
    public void setCreationRadius(final int creationRadius) {
        this.creationRadius = creationRadius;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityPortalEvent.getHandlerList();
    }
}
