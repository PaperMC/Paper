package org.bukkit.craftbukkit.event.vehicle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.util.CraftVector;
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

    public CraftVehicleBlockCollisionEvent(final Vehicle vehicle, final Level level, final BlockPos pos, final Vec3 movement) {
        this(vehicle, CraftBlock.at(level, pos), CraftVector.toBukkit(movement));
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
