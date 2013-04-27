package org.bukkit.craftbukkit;

import net.minecraft.server.ChunkCoordinates;
import net.minecraft.server.PortalTravelAgent;
import net.minecraft.server.WorldServer;

import org.bukkit.Location;
import org.bukkit.TravelAgent;

public class CraftTravelAgent extends PortalTravelAgent implements TravelAgent {

    public static TravelAgent DEFAULT = null;

    private int searchRadius = 128;
    private int creationRadius = 16;
    private boolean canCreatePortal = true;

    public CraftTravelAgent(WorldServer worldserver) {
        super(worldserver);
        if (DEFAULT == null && worldserver.dimension == 0) {
            DEFAULT = this;
        }
    }

    public Location findOrCreate(Location target) {
        WorldServer worldServer = ((CraftWorld) target.getWorld()).getHandle();
        boolean before = worldServer.chunkProviderServer.forceChunkLoad;
        worldServer.chunkProviderServer.forceChunkLoad = true;

        Location found = this.findPortal(target);
        if (found == null) {
            if (this.getCanCreatePortal() && this.createPortal(target)) {
                found = this.findPortal(target);
            } else {
                found = target; // fallback to original if unable to find or create
            }
        }

        worldServer.chunkProviderServer.forceChunkLoad = before;
        return found;
    }

    public Location findPortal(Location location) {
        PortalTravelAgent pta = ((CraftWorld) location.getWorld()).getHandle().t(); // Should be getTravelAgent
        ChunkCoordinates found = pta.findPortal(location.getX(), location.getY(), location.getZ(), this.getSearchRadius());
        return found != null ? new Location(location.getWorld(), found.x, found.y, found.z, location.getYaw(), location.getPitch()) : null;
    }

    public boolean createPortal(Location location) {
        PortalTravelAgent pta = ((CraftWorld) location.getWorld()).getHandle().t();
        return pta.createPortal(location.getX(), location.getY(), location.getZ(), this.getCreationRadius());
    }

    public TravelAgent setSearchRadius(int radius) {
        this.searchRadius = radius;
        return this;
    }

    public int getSearchRadius() {
        return this.searchRadius;
    }

    public TravelAgent setCreationRadius(int radius) {
        this.creationRadius = radius < 2 ? 0 : radius;
        return this;
    }

    public int getCreationRadius() {
        return this.creationRadius;
    }

    public boolean getCanCreatePortal() {
        return this.canCreatePortal;
    }

    public void setCanCreatePortal(boolean create) {
        this.canCreatePortal = create;
    }
}
