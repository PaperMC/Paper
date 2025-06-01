package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.CalibratedSculkSensor;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftCalibratedSculkSensor extends CraftBlockData implements CalibratedSculkSensor {
    private static final EnumProperty<Direction> FACING = CalibratedSculkSensorBlock.FACING;

    private static final IntegerProperty POWER = CalibratedSculkSensorBlock.POWER;

    private static final EnumProperty<SculkSensorPhase> PHASE = CalibratedSculkSensorBlock.PHASE;

    private static final BooleanProperty WATERLOGGED = CalibratedSculkSensorBlock.WATERLOGGED;

    public CraftCalibratedSculkSensor(BlockState state) {
        super(state);
    }

    @Override
    public BlockFace getFacing() {
        return this.get(FACING, BlockFace.class);
    }

    @Override
    public void setFacing(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace.isCartesian() && blockFace.getModY() == 0, "Invalid face, only cartesian horizontal face are allowed for this property!");
        this.set(FACING, blockFace);
    }

    @Override
    public Set<BlockFace> getFaces() {
        return this.getValues(FACING, BlockFace.class);
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
    public org.bukkit.block.data.type.SculkSensor.Phase getSculkSensorPhase() {
        return this.get(PHASE, org.bukkit.block.data.type.SculkSensor.Phase.class);
    }

    @Override
    public void setSculkSensorPhase(final org.bukkit.block.data.type.SculkSensor.Phase phase) {
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
