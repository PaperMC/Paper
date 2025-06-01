package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftBeehive extends CraftBlockData implements Beehive {
    private static final EnumProperty<Direction> FACING = BeehiveBlock.FACING;

    private static final IntegerProperty HONEY_LEVEL = BeehiveBlock.HONEY_LEVEL;

    public CraftBeehive(BlockState state) {
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
    public int getHoneyLevel() {
        return this.get(HONEY_LEVEL);
    }

    @Override
    public void setHoneyLevel(final int honeyLevel) {
        this.set(HONEY_LEVEL, honeyLevel);
    }

    @Override
    public int getMaximumHoneyLevel() {
        return HONEY_LEVEL.max;
    }
}
