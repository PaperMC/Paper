package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.SculkSensor;

public class CraftSculkSensor<T extends SculkSensorBlockEntity> extends CraftBlockEntityState<T> implements SculkSensor {

    public CraftSculkSensor(World world, T tileEntity) {
        super(world, tileEntity);
    }

    protected CraftSculkSensor(CraftSculkSensor<T> state, Location location) {
        super(state, location);
    }

    @Override
    public int getLastVibrationFrequency() {
        return getSnapshot().getLastVibrationFrequency();
    }

    @Override
    public void setLastVibrationFrequency(int lastVibrationFrequency) {
        Preconditions.checkArgument(0 <= lastVibrationFrequency && lastVibrationFrequency <= 15, "Vibration frequency must be between 0-15");
        getSnapshot().lastVibrationFrequency = lastVibrationFrequency;
    }

    @Override
    public CraftSculkSensor<T> copy() {
        return new CraftSculkSensor<>(this, null);
    }

    @Override
    public CraftSculkSensor<T> copy(Location location) {
        return new CraftSculkSensor<>(this, location);
    }
}
