package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityCreeper;
import net.minecraft.server.WorldServer;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.CreeperPowerEvent;

public class CraftCreeper extends CraftMonster implements Creeper {

    public CraftCreeper(CraftServer server, EntityCreeper entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCreeper";
    }

    public boolean isPowered() {
        return getHandle().W().a(17) == 1;
    }

    public void setPowered(boolean powered) {
        // CraftBukkit start
        CraftServer server = this.server;
        org.bukkit.entity.Entity entity = this.getHandle().getBukkitEntity();

        if (powered) {
            CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_ON);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                getHandle().W().b(17, (byte)1);
            }
        } else {
            CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_OFF);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                getHandle().W().b(17, (byte)0);
            }
        }

        // CraftBukkit end

    }

}
