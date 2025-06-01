package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftSapling extends CraftBlockData implements Sapling {
    private static final IntegerProperty STAGE = SaplingBlock.STAGE;

    public CraftSapling(BlockState state) {
        super(state);
    }

    @Override
    public int getStage() {
        return this.get(STAGE);
    }

    @Override
    public void setStage(final int stage) {
        this.set(STAGE, stage);
    }

    @Override
    public int getMaximumStage() {
        return STAGE.max;
    }
}
