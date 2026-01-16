package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftTarget extends CraftBlockData implements AnaloguePowerable {
    private static final IntegerProperty OUTPUT_POWER = BlockStateProperties.POWER;

    public CraftTarget(BlockState state) {
        super(state);
    }

    @Override
    public int getPower() {
        return this.get(OUTPUT_POWER);
    }

    @Override
    public void setPower(final int power) {
        this.set(OUTPUT_POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return OUTPUT_POWER.max;
    }
}
