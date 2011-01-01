
package org.bukkit.craftbukkit;

import net.minecraft.server.Entity;
import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;

public class CraftEntity implements org.bukkit.Entity {
    protected final CraftServer server;
    private Entity entity;

    public CraftEntity(final CraftServer server, final Entity entity) {
        this.server = server;
        this.entity = entity;
    }

    public Location getLocation() {
        return new Location(getWorld(), entity.p, entity.q, entity.r, entity.v, entity.w);
    }

    public World getWorld() {
        return ((WorldServer)entity.l).getWorld();
    }

    public void teleportTo(Location location) {
        entity.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public int getEntityID() {
        return entity.g;
    }

    public Entity getHandle() {
        return entity;
    }

    public void setHandle(final Entity entity) {
        this.entity = entity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftEntity other = (CraftEntity) obj;
        if (this.server != other.server && (this.server == null || !this.server.equals(other.server))) {
            return false;
        }
        if (this.entity != other.entity && (this.entity == null || !this.entity.equals(other.entity))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.server != null ? this.server.hashCode() : 0);
        hash = 89 * hash + (this.entity != null ? this.entity.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CraftEntity{" + "id=" + getEntityID() + '}';
    }
}
