package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Vehicle;

public abstract class CraftVehicle extends CraftEntity implements Vehicle {

    public CraftVehicle(CraftServer server, net.minecraft.world.entity.Entity entity) {
        super(server, entity);
    }
}
