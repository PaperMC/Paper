
package org.bukkit.craftbukkit;

import net.minecraft.server.EntityPlayerMP;
import net.minecraft.server.Packet3Chat;
import org.bukkit.Location;
import org.bukkit.Player;

public class CraftPlayer extends CraftHumanEntity implements Player {
    private EntityPlayerMP entity;

    public CraftPlayer(CraftServer server, EntityPlayerMP entity) {
        super(server, entity);
        this.entity = entity;
    }

    public boolean isOnline() {
        return server.getHandle().g(getName());
    }

    @Override
    public EntityPlayerMP getHandle() {
        return entity;
    }

    public void setHandle(final EntityPlayerMP entity) {
        this.entity = entity;
    }

    public void sendMessage(String message) {
        entity.a.b(new Packet3Chat(message));
    }

    @Override
    public void teleportTo(Location location) {
        entity.a.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }
}
