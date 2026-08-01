package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.Snow;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftSnowLayer extends CraftBlockData implements Snow {
    private static final IntegerProperty LAYERS = SnowLayerBlock.LAYERS;

    public CraftSnowLayer(BlockState state) {
        super(state);
    }

    @Override
    public int getLayers() {
        return this.get(LAYERS);
    }

    @Override
    public void setLayers(final int layers) {
        this.set(LAYERS, layers);
    }

    @Override
    public int getMinimumLayers() {
        return LAYERS.min;
    }

    @Override
    public int getMaximumLayers() {
        return LAYERS.max;
    }
}
