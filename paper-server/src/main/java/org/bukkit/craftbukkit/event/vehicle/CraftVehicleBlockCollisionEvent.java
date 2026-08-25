package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.util.Vector;

public class CraftVehicleBlockCollisionEvent extends CraftVehicleCollisionEvent implements VehicleBlockCollisionEvent {

    private final Block block;
    private final Vector velocity;

    public CraftVehicleBlockCollisionEvent(final Vehicle vehicle, final Block block, final Vector velocity) {
        super(vehicle);
        this.block = block;
        this.velocity = velocity;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public Vector getVelocity() {
        return this.velocity.clone();
    }
}
