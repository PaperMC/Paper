package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.Brushable;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftBrushable extends CraftBlockData implements Brushable {
    private static final IntegerProperty DUSTED = BlockStateProperties.DUSTED;

    public CraftBrushable(BlockState state) {
        super(state);
    }

    @Override
    public int getDusted() {
        return this.get(DUSTED);
    }

    @Override
    public void setDusted(final int dusted) {
        this.set(DUSTED, dusted);
    }

    @Override
    public int getMaximumDusted() {
        return DUSTED.max;
    }
}
