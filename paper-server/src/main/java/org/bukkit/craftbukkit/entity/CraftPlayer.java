package org.bukkit.craftbukkit.entity;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.Packet6SpawnPosition;
import net.minecraft.server.ServerConfigurationManager;
import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.TextWrapper;
import org.bukkit.entity.Player;

public class CraftPlayer extends CraftHumanEntity implements Player {

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
        if (ignoreSneaking) {
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

    public void sendRawMessage(String message) {
        getHandle().a.b(new Packet3Chat(message));
    }

    public void sendMessage(String message) {
        for (final String line: TextWrapper.wrapText(message)) {
            getHandle().a.b(new Packet3Chat(line));
        }
    }

    public String getDisplayName() {
        return getHandle().displayName;
    }

    public void setDisplayName(final String name) {
        getHandle().displayName = name;
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
        // Do not directly assign here, from the packethandler we'll assign it.
        getHandle().a.b((Packet) new Packet6SpawnPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    public Location getCompassTarget() {
        return getHandle().compassTarget;
    }

    public void chat(String msg) {
        getHandle().a.chat(msg);
    }

    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public boolean teleport(Location location) {
        WorldServer oldWorld = ((CraftWorld)getWorld()).getHandle();
        WorldServer newWorld = ((CraftWorld)location.getWorld()).getHandle();
        ServerConfigurationManager manager = server.getHandle();
        EntityPlayer entity = getHandle();
        boolean teleportSuccess;

        if (oldWorld != newWorld) {

            EntityPlayer newEntity = new EntityPlayer(manager.c, newWorld, entity.name, new ItemInWorldManager(newWorld));

            newEntity.id = entity.id;
            newEntity.a = entity.a;
            newEntity.health = entity.health;
            newEntity.fireTicks = entity.fireTicks;
            newEntity.inventory = entity.inventory;
            newEntity.inventory.d = newEntity;
            newEntity.activeContainer = entity.activeContainer;
            newEntity.defaultContainer = entity.defaultContainer;
            newEntity.locX = location.getX();
            newEntity.locY = location.getY();
            newEntity.locZ = location.getZ();
            newEntity.displayName = entity.displayName;
            newEntity.compassTarget = entity.compassTarget;
            newWorld.u.c((int) location.getBlockX() >> 4, (int) location.getBlockZ() >> 4);

            teleportSuccess = newEntity.a.teleport(location);

            if (teleportSuccess) {
                manager.c.k.a(entity);
                manager.c.k.b(entity);
                oldWorld.manager.b(entity);
                manager.b.remove(entity);
                oldWorld.e(entity);

                newWorld.manager.a(newEntity);
                newWorld.a(newEntity);
                manager.b.add(newEntity);

                entity.a.e = newEntity;
                this.entity = newEntity;

                setCompassTarget(getCompassTarget());
            }

            return teleportSuccess;
        } else {
            return entity.a.teleport(location);
        }
    }

    public void setSneaking(boolean sneak) {
        getHandle().e(sneak);
    }

    public boolean isSneaking() {
        return getHandle().Z();
    }

    public void loadData() {
        server.getHandle().n.b(getHandle());
    }

    public void saveData() {
        server.getHandle().n.a(getHandle());
    }

    public void updateInventory() {
        getHandle().m();
    }
}
