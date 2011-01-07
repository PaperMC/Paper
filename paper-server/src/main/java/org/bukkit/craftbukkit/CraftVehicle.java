package org.bukkit.craftbukkit;

import org.bukkit.Entity;
import org.bukkit.Vector;
import org.bukkit.Vehicle;

/**
 * A vehicle.
 * 
 * @author sk89q
 */
public abstract class CraftVehicle extends CraftEntity implements Vehicle {
    private net.minecraft.server.Entity vehicle;
    
    public CraftVehicle(CraftServer server, net.minecraft.server.Entity entity) {
        super(server, entity);
        vehicle = entity;
    }

    public Entity getPassenger() {
        return ((CraftWorld)getWorld()).toCraftEntity(getHandle().j);
    }

    public boolean isEmpty() {
        return getHandle().j == null;
    }

    public Vector getVelocity() {
        return new Vector(vehicle.s, vehicle.t, vehicle.u);
    }

    public void setVelocity(Vector vel) {
        vehicle.s = vel.getX();
        vehicle.t = vel.getY();
        vehicle.u = vel.getZ();
    }
}
