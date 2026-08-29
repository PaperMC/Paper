package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ChestRaft;

public class CraftChestRaft extends CraftChestBoat implements ChestRaft {

    public CraftChestRaft(final CraftServer server, final net.minecraft.world.entity.vehicle.boat.ChestRaft entity) {
        super(server, entity);
    }
}
