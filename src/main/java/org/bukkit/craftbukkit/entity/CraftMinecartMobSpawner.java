package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMinecartMobSpawner;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MinecartMobSpawner;

final class CraftMinecartMobSpawner extends CraftMinecart implements MinecartMobSpawner {
    CraftMinecartMobSpawner(CraftServer server, EntityMinecartMobSpawner entity) {
        super(server, entity);
    }

    public EntityType getType() {
        return EntityType.MINECART_MOB_SPAWNER;
    }
}
