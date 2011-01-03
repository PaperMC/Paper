package org.bukkit.craftbukkit;

import net.minecraft.server.Entity;

import org.bukkit.Vehicle;

/**
 * A vehicle.
 * 
 * @author sk89q
 */
public abstract class CraftVehicle extends CraftEntity implements Vehicle {
    public CraftVehicle(CraftServer server, Entity entity) {
        super(server, entity);
    }
}
