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
        to = portalEvent.getTo();
        searchRadius = portalEvent.getSearchRadius();
        cancelled = portalEvent.isCancelled();
        creationRadius = 0;
        canCreatePortal = false;
    }

    public CraftPortalEvent(PlayerPortalEvent portalEvent) {
        to = portalEvent.getTo();
        searchRadius = portalEvent.getSearchRadius();
        creationRadius = portalEvent.getCreationRadius();
        canCreatePortal = portalEvent.getCanCreatePortal();
        cancelled = portalEvent.isCancelled();
    }

    public Location getTo() {
        return to;
    }

    public int getSearchRadius() {
        return searchRadius;
    }

    public int getCreationRadius() {
        return creationRadius;
    }

    public boolean getCanCreatePortal() {
        return canCreatePortal;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
