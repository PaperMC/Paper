package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMinecartMobSpawner;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.SpawnerMinecart;

final class CraftMinecartMobSpawner extends CraftMinecart implements SpawnerMinecart {
    CraftMinecartMobSpawner(CraftServer server, EntityMinecartMobSpawner entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartMobSpawner";
    }

    public EntityType getType() {
        return EntityType.MINECART_MOB_SPAWNER;
    }
}
