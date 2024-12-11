package org.bukkit.craftbukkit.event;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

/**
 * Helper class to hold information from the {@link PlayerPortalEvent} and {@link EntityPortalEvent}
 */
public class CraftPortalEvent {
    private final Location to;
    private final int searchRadius;
    private final int creationRadius;

    private final boolean canCreatePortal;
    private final boolean cancelled;

    public CraftPortalEvent(EntityPortalEvent portalEvent) {
        this.to = portalEvent.getTo();
        this.searchRadius = portalEvent.getSearchRadius();
        this.cancelled = portalEvent.isCancelled();
        this.creationRadius = portalEvent.getCreationRadius();
        this.canCreatePortal = portalEvent.getCanCreatePortal();
    }

    public CraftPortalEvent(PlayerPortalEvent portalEvent) {
        this.to = portalEvent.getTo();
        this.searchRadius = portalEvent.getSearchRadius();
        this.creationRadius = portalEvent.getCreationRadius();
        this.canCreatePortal = portalEvent.getCanCreatePortal();
        this.cancelled = portalEvent.isCancelled();
    }

    public Location getTo() {
        return this.to;
    }

    public int getSearchRadius() {
        return this.searchRadius;
    }

    public int getCreationRadius() {
        return this.creationRadius;
    }

    public boolean getCanCreatePortal() {
        return this.canCreatePortal;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
}
