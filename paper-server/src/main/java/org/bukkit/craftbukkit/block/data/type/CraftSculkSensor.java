package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.SculkSensor;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftSculkSensor extends CraftBlockData implements SculkSensor {

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> PHASE = getEnum("sculk_sensor_phase");

    @Override
    public org.bukkit.block.data.type.SculkSensor.Phase getPhase() {
        return get(PHASE, org.bukkit.block.data.type.SculkSensor.Phase.class);
    }

    @Override
    public void setPhase(org.bukkit.block.data.type.SculkSensor.Phase phase) {
        set(PHASE, phase);
    }
}
