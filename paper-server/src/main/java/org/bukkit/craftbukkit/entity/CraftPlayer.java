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
import org.bukkit.craftbukkit.TextWrapper;
import org.bukkit.entity.Player;

public class CraftPlayer extends CraftHumanEntity implements Player {
    private String name;

    public CraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
    }

    public boolean isOp() {
        return server.getHandle().h(getName());
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
        SocketAddress addr = getHandle().a.b.b();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public EntityPlayer getHandle() {
        return (EntityPlayer) entity;
    }

    public double getEyeHeight() {
        return getEyeHeight(false);
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        if(ignoreSneaking) {
            return 1.62D;
        } else {
            if (isSneaking()) {
                return 1.42D;
            } else {
                return 1.62D;
            }
        }
    }

    public void setHandle(final EntityPlayer entity) {
        super.setHandle((EntityHuman) entity);
        this.entity = entity;
    }

    public void sendMessage(String message) {
        for (final String line: TextWrapper.wrapText(message)) {
            getHandle().a.b(new Packet3Chat(line));
        }
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
        getHandle().a.a(message == null ? "" : message);
    }

    public void setCompassTarget(Location loc) {
        getHandle().a.b(((Packet) (new Packet6SpawnPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))));
    }

    public void chat(String msg) {
        getHandle().a.chat(msg);
    }

    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public void teleportTo(Location location) {
        WorldServer oldWorld = ((CraftWorld)getWorld()).getHandle();
        WorldServer newWorld = ((CraftWorld)location.getWorld()).getHandle();
        ServerConfigurationManager manager = server.getHandle();
        EntityPlayer entity = getHandle();

        if (oldWorld != newWorld) {
            manager.c.k.a(entity);
            manager.c.k.b(entity);
            oldWorld.manager.b(entity);
            manager.b.remove(entity);
            oldWorld.e(entity);

            EntityPlayer newEntity = new EntityPlayer(manager.c, newWorld, entity.name, new ItemInWorldManager(newWorld));

            newEntity.id = entity.id;
            newEntity.a = entity.a;
            newEntity.health = entity.health;
            newEntity.fireTicks = entity.fireTicks;
            newEntity.inventory = entity.inventory;
            newEntity.inventory.e = newEntity;
            newEntity.activeContainer = entity.activeContainer;
            newEntity.defaultContainer = entity.defaultContainer;
            newWorld.u.d((int) location.getBlockX() >> 4, (int) location.getBlockZ() >> 4);

            newEntity.a.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            newWorld.manager.a(newEntity);
            newWorld.a(newEntity);
            manager.b.add(newEntity);

            entity.a.e = newEntity;
            this.entity = newEntity;
        } else {
            entity.a.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }
    }

    public void setSneaking(boolean sneak) {
        getHandle().b(sneak);
    }

    public boolean isSneaking() {
        return getHandle().U();
    }

    public void updateInventory() {
        getHandle().l();
    }
}
