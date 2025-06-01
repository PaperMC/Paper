package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftLantern extends CraftBlockData implements Lantern {
    private static final BooleanProperty HANGING = LanternBlock.HANGING;

    private static final BooleanProperty WATERLOGGED = LanternBlock.WATERLOGGED;

    public CraftLantern(BlockState state) {
        super(state);
    }

    @Override
    public boolean isHanging() {
        return this.get(HANGING);
    }

    @Override
    public void setHanging(final boolean hanging) {
        this.set(HANGING, hanging);
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
