package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CalibratedSculkSensor;

public class CraftCalibratedSculkSensor extends CraftSculkSensor<CalibratedSculkSensorBlockEntity> implements CalibratedSculkSensor {

    public CraftCalibratedSculkSensor(World world, CalibratedSculkSensorBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftCalibratedSculkSensor(CraftCalibratedSculkSensor state, Location location) {
        super(state, location);
    }

    @Override
    public CraftCalibratedSculkSensor copy() {
        return new CraftCalibratedSculkSensor(this, null);
    }

    @Override
    public CraftCalibratedSculkSensor copy(Location location) {
        return new CraftCalibratedSculkSensor(this, location);
    }
}
