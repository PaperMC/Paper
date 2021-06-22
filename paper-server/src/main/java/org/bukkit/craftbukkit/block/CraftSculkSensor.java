package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.SculkSensor;

public class CraftSculkSensor extends CraftBlockEntityState<SculkSensorBlockEntity> implements SculkSensor {

    public CraftSculkSensor(final Block block) {
        super(block, SculkSensorBlockEntity.class);
    }

    public CraftSculkSensor(final Material material, final SculkSensorBlockEntity te) {
        super(material, te);
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
}
