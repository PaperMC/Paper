package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.data.Bisected;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.5")
public class CraftTallSeagrass extends CraftBlockData implements Bisected {
    private static final EnumProperty<DoubleBlockHalf> HALF = TallSeagrassBlock.HALF;

    public CraftTallSeagrass(BlockState state) {
        super(state);
    }

    @Override
    public Bisected.Half getHalf() {
        return this.get(HALF, Bisected.Half.class);
    }

    @Override
    public void setHalf(final Bisected.Half half) {
        Preconditions.checkArgument(half != null, "half cannot be null!");
        this.set(HALF, half);
    }
}
