package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.Snowable;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftGrass extends CraftBlockData implements Snowable {
    private static final BooleanProperty SNOWY = GrassBlock.SNOWY;

    public CraftGrass(BlockState state) {
        super(state);
    }

    @Override
    public boolean isSnowy() {
        return this.get(SNOWY);
    }

    @Override
    public void setSnowy(final boolean snowy) {
        this.set(SNOWY, snowy);
    }
}
