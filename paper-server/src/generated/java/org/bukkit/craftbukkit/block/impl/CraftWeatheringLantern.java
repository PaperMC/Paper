package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.WeatheringLanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftWeatheringLantern extends CraftBlockData implements Lantern {
    private static final BooleanProperty HANGING = WeatheringLanternBlock.HANGING;

    private static final BooleanProperty WATERLOGGED = WeatheringLanternBlock.WATERLOGGED;

    public CraftWeatheringLantern(BlockState state) {
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
