package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.WeatheringCopperSlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.bukkit.block.data.type.Slab;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftWeatheringCopperSlab extends CraftBlockData implements Slab {
    private static final EnumProperty<SlabType> TYPE = WeatheringCopperSlabBlock.TYPE;

    private static final BooleanProperty WATERLOGGED = WeatheringCopperSlabBlock.WATERLOGGED;

    public CraftWeatheringCopperSlab(BlockState state) {
        super(state);
    }

    @Override
    public Slab.Type getType() {
        return this.get(TYPE, Slab.Type.class);
    }

    @Override
    public void setType(final Slab.Type type) {
        Preconditions.checkArgument(type != null, "type cannot be null!");
        this.set(TYPE, type);
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
