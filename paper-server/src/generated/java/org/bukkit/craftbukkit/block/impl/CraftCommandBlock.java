package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftCommandBlock extends CraftBlockData implements CommandBlock {
    private static final BooleanProperty CONDITIONAL = net.minecraft.world.level.block.CommandBlock.CONDITIONAL;

    private static final EnumProperty<Direction> FACING = net.minecraft.world.level.block.CommandBlock.FACING;

    public CraftCommandBlock(BlockState state) {
        super(state);
    }

    @Override
    public boolean isConditional() {
        return this.get(CONDITIONAL);
    }

    @Override
    public void setConditional(final boolean conditional) {
        this.set(CONDITIONAL, conditional);
    }

    @Override
    public BlockFace getFacing() {
        return this.get(FACING, BlockFace.class);
    }

    @Override
    public void setFacing(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace.isCartesian(), "Invalid face, only cartesian face are allowed for this property!");
        this.set(FACING, blockFace);
    }

    @Override
    public Set<BlockFace> getFaces() {
        return this.getValues(FACING, BlockFace.class);
    }
}
