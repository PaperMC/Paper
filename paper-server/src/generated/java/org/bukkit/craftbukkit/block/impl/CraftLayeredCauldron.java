package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.Levelled;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftLayeredCauldron extends CraftBlockData implements Levelled {
    private static final IntegerProperty LEVEL = LayeredCauldronBlock.LEVEL;

    public CraftLayeredCauldron(BlockState state) {
        super(state);
    }

    @Override
    public int getLevel() {
        return this.get(LEVEL);
    }

    @Override
    public void setLevel(final int level) {
        this.set(LEVEL, level);
    }

    @Override
    public int getMinimumLevel() {
        return LEVEL.min;
    }

    @Override
    public int getMaximumLevel() {
        return LEVEL.max;
    }
}
