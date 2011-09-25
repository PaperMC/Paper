package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Boat;

public class CraftBoat extends CraftVehicle implements Boat {
    protected EntityBoat boat;

    public CraftBoat(CraftServer server, EntityBoat entity) {
        super(server, entity);
        boat = entity;
    }

    public double getMaxSpeed() {
        return boat.maxSpeed;
    }

    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            boat.maxSpeed = speed;
        }
    }

    public double getOccupiedDeceleration() {
        return boat.occupiedDeceleration;
    }

    public void setOccupiedDeceleration(double speed) {
        if (speed >= 0D) {
            boat.occupiedDeceleration = speed;
        }
    }

    public double getUnoccupiedDeceleration() {
        return boat.unoccupiedDeceleration;
    }

    public void setUnoccupiedDeceleration(double speed) {
        boat.unoccupiedDeceleration = speed;
    }

    public boolean getWorkOnLand() {
        return boat.landBoats;
    }

    public void setWorkOnLand(boolean workOnLand) {
        boat.landBoats = workOnLand;
    }

    @Override
    public String toString() {
        return "CraftBoat";
    }
}
