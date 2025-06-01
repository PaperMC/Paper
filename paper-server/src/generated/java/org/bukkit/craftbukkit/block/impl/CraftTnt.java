package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.type.TNT;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftTnt extends CraftBlockData implements TNT {
    private static final BooleanProperty UNSTABLE = TntBlock.UNSTABLE;

    public CraftTnt(BlockState state) {
        super(state);
    }

    @Override
    public boolean isUnstable() {
        return this.get(UNSTABLE);
    }

    @Override
    public void setUnstable(final boolean unstable) {
        this.set(UNSTABLE, unstable);
    }
}
