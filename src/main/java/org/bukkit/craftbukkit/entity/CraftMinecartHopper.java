package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMinecartHopper;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MinecartHopper;

final class CraftMinecartHopper extends CraftMinecart implements MinecartHopper {
    CraftMinecartHopper(CraftServer server, EntityMinecartHopper entity) {
        super(server, entity);
    }

    public EntityType getType() {
        return EntityType.MINECART_HOPPER;
    }
}
