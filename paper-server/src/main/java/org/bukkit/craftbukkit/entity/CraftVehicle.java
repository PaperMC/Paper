package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;

/**
 * A vehicle.
 * 
 * @author sk89q
 */
public abstract class CraftVehicle extends CraftEntity implements Vehicle {
    public CraftVehicle(CraftServer server, net.minecraft.server.Entity entity) {
        super(server, entity);
    }

    public Entity getPassenger() {
        return (getHandle().j.getBukkitEntity());
    }
    
    public boolean setPassenger(Entity passenger) {
        ((CraftEntity)passenger).getHandle().setPassengerOf(getHandle());
        return true;
    }

    public boolean isEmpty() {
        return getHandle().j == null;
    }
    
    public boolean eject() {
        if (getHandle().j == null) {
            return false;
        }
        
        getHandle().j.setPassengerOf(null);
        return true;
    }
}
