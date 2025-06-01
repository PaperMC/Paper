package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import org.bukkit.block.data.type.SculkSensor;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftSculkSensor extends CraftBlockData implements SculkSensor {
    private static final IntegerProperty POWER = SculkSensorBlock.POWER;

    private static final EnumProperty<SculkSensorPhase> PHASE = SculkSensorBlock.PHASE;

    private static final BooleanProperty WATERLOGGED = SculkSensorBlock.WATERLOGGED;

    public CraftSculkSensor(BlockState state) {
        super(state);
    }

    @Override
    public int getPower() {
        return this.get(POWER);
    }

    @Override
    public void setPower(final int power) {
        this.set(POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return POWER.max;
    }

    @Override
    public SculkSensor.Phase getSculkSensorPhase() {
        return this.get(PHASE, SculkSensor.Phase.class);
    }

    @Override
    public void setSculkSensorPhase(final SculkSensor.Phase phase) {
        Preconditions.checkArgument(phase != null, "phase cannot be null!");
        this.set(PHASE, phase);
    }

    @Override
    public boolean isWaterlogged() {
        return this.get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(final boolean waterlogged) {
        this.set(WATERLOGGED, waterlogged);
    }
}
