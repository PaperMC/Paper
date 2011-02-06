package org.bukkit.craftbukkit.entity;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.Packet6SpawnPosition;
import net.minecraft.server.Packet9Respawn;
import net.minecraft.server.ServerConfigurationManager;
import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

public class CraftPlayer extends CraftHumanEntity implements Player {
    private EntityPlayer entity;
    private String name;

    public CraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
        this.name = getName();
        this.entity = entity;
    }

    public boolean isOp() {
        return server.getHandle().g(getName());
    }

    public boolean isPlayer() {
        return true;
    }

    public boolean isOnline() {
        for (Object obj: server.getHandle().b) {
            EntityPlayer player = (EntityPlayer) obj;
            if (player.name.equalsIgnoreCase(getName())) {
                return true;
            }
        }
        return false;
    }

    public InetSocketAddress getAddress() {
        SocketAddress addr = entity.a.b.b();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public EntityPlayer getHandle() {
        return entity;
    }

    public void setHandle(final EntityPlayer entity) {
        super.setHandle((EntityHuman) entity);
        this.entity = entity;
    }

    public void sendMessage(String message) {
        entity.a.b(new Packet3Chat(message));
    }

    public String getDisplayName() {
        return name;
    }

    public void setDisplayName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftPlayer other = (CraftPlayer) obj;
        if ((this.getName() == null) ? (other.getName() != null) : !this.getName().equals(other.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.getName() != null ? this.getName().hashCode() : 0);
        return hash;
    }

    public void kickPlayer(String message) {
        entity.a.a(message == null ? "" : message);
    }

    public void setCompassTarget(Location loc) {
        entity.a.b(((Packet) (new Packet6SpawnPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))));
    }

    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public void teleportTo(Location location) {
        boolean worldChange = location.getWorld() != getWorld();
        ServerConfigurationManager manager = server.getHandle();

        if (worldChange) {
            manager.c.k.a(entity);
            manager.c.k.b(entity);
            manager.d.b(entity);
            entity.world.e(entity);

            entity.world = ((CraftWorld)location.getWorld()).getHandle();

            entity.c = new ItemInWorldManager(((CraftWorld)location.getWorld()).getHandle());
            entity.c.a = entity;

            ((WorldServer)entity.world).A.d((int) entity.locX >> 4, (int) entity.locZ >> 4);
        }

        entity.a.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        if (worldChange) {
            manager.d.a(entity);
            entity.world.a(entity);
        }
    }
}
