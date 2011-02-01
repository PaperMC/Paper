package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
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
        return isEmpty() ? null : (getHandle().passenger.getBukkitEntity());
    }

    public boolean setPassenger(Entity passenger) {
        if(passenger instanceof CraftEntity){
            ((CraftEntity)passenger).getHandle().setPassengerOf(getHandle());
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return getHandle().passenger == null;
    }

    public boolean eject() {
        if (getHandle().passenger == null) {
            return false;
        }

        getHandle().passenger.setPassengerOf(null);
        return true;
    }

    @Override
    public String toString() {
        return "CraftVehicle{passenger=" + getPassenger() + '}';
    }
}
