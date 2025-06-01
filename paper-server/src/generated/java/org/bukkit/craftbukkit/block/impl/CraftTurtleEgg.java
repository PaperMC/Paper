package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.TurtleEgg;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftTurtleEgg extends CraftBlockData implements TurtleEgg {
    private static final IntegerProperty EGGS = TurtleEggBlock.EGGS;

    private static final IntegerProperty HATCH = TurtleEggBlock.HATCH;

    public CraftTurtleEgg(BlockState state) {
        super(state);
    }

    @Override
    public int getEggs() {
        return this.get(EGGS);
    }

    @Override
    public void setEggs(final int eggs) {
        this.set(EGGS, eggs);
    }

    @Override
    public int getMinimumEggs() {
        return EGGS.min;
    }

    @Override
    public int getMaximumEggs() {
        return EGGS.max;
    }

    @Override
    public int getHatch() {
        return this.get(HATCH);
    }

    @Override
    public void setHatch(final int hatch) {
        this.set(HATCH, hatch);
    }

    @Override
    public int getMaximumHatch() {
        return HATCH.max;
    }
}
