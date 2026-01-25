package org.bukkit.craftbukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerPortalEvent;
import org.jspecify.annotations.Nullable;

public class CraftPlayerPortalEvent extends CraftPlayerTeleportEvent implements PlayerPortalEvent {

    private int searchRadius;
    private boolean canCreatePortal;
    private int creationRadius;

    public CraftPlayerPortalEvent(final Player player, final Location from, final @Nullable Location to, final TeleportCause cause, final int searchRadius, final boolean canCreatePortal, final int creationRadius) {
        super(player, from, to, cause);
        this.searchRadius = searchRadius;
        this.canCreatePortal = canCreatePortal;
        this.creationRadius = creationRadius;
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
    public boolean getCanCreatePortal() {
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
        return PlayerPortalEvent.getHandlerList();
    }
}
