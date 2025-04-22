package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.CoralFanBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.5")
public class CraftCoralFan extends CraftBlockData implements Waterlogged {
    private static final BooleanProperty WATERLOGGED = CoralFanBlock.WATERLOGGED;

    public CraftCoralFan(BlockState state) {
        super(state);
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
