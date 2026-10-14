package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.CaveVinesPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftCaveVinesPlant extends CraftBlockData implements CaveVinesPlant {
    private static final BooleanProperty BERRIES = CaveVinesPlantBlock.BERRIES;

    public CraftCaveVinesPlant(BlockState state) {
        super(state);
    }

    @Override
    public boolean hasBerries() {
        return this.get(BERRIES);
    }

    @Override
    public void setBerries(final boolean berries) {
        this.set(BERRIES, berries);
    }
}
