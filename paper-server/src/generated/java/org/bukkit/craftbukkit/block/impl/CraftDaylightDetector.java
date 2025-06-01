package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.DaylightDetectorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftDaylightDetector extends CraftBlockData implements DaylightDetector {
    private static final BooleanProperty INVERTED = DaylightDetectorBlock.INVERTED;

    private static final IntegerProperty POWER = DaylightDetectorBlock.POWER;

    public CraftDaylightDetector(BlockState state) {
        super(state);
    }

    @Override
    public boolean isInverted() {
        return this.get(INVERTED);
    }

    @Override
    public void setInverted(final boolean inverted) {
        this.set(INVERTED, inverted);
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
}
