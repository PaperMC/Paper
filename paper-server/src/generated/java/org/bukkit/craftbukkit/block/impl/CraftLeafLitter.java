package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.LeafLitterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.LeafLitter;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftLeafLitter extends CraftBlockData implements LeafLitter {
    private static final EnumProperty<Direction> FACING = LeafLitterBlock.FACING;

    private static final IntegerProperty AMOUNT = LeafLitterBlock.AMOUNT;

    public CraftLeafLitter(BlockState state) {
        super(state);
    }

    @Override
    public BlockFace getFacing() {
        return this.get(FACING, BlockFace.class);
    }

    @Override
    public void setFacing(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace.isCartesian() && blockFace.getModY() == 0, "Invalid face, only cartesian horizontal face are allowed for this property!");
        this.set(FACING, blockFace);
    }

    @Override
    public Set<BlockFace> getFaces() {
        return this.getValues(FACING, BlockFace.class);
    }

    @Override
    public int getSegmentAmount() {
        return this.get(AMOUNT);
    }

    @Override
    public void setSegmentAmount(final int segmentAmount) {
        this.set(AMOUNT, segmentAmount);
    }

    @Override
    public int getMinimumSegmentAmount() {
        return AMOUNT.min;
    }

    @Override
    public int getMaximumSegmentAmount() {
        return AMOUNT.max;
    }
}
