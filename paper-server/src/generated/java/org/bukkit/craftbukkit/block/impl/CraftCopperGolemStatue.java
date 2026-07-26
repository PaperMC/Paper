package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.annotation.GeneratedClass;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CopperGolemStatueBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.CopperGolemStatue;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftCopperGolemStatue extends CraftBlockData implements CopperGolemStatue {
    private static final EnumProperty<CopperGolemStatueBlock.Pose> POSE = CopperGolemStatueBlock.POSE;

    private static final EnumProperty<Direction> FACING = CopperGolemStatueBlock.FACING;

    private static final BooleanProperty WATERLOGGED = CopperGolemStatueBlock.WATERLOGGED;

    public CraftCopperGolemStatue(BlockState state) {
        super(state);
    }

    @Override
    public CopperGolemStatue.Pose getCopperGolemPose() {
        return this.get(POSE, CopperGolemStatue.Pose.class);
    }

    @Override
    public void setCopperGolemPose(final CopperGolemStatue.Pose pose) {
        Preconditions.checkArgument(pose != null, "pose cannot be null!");
        this.set(POSE, pose);
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
    public boolean isWaterlogged() {
        return this.get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(final boolean waterlogged) {
        this.set(WATERLOGGED, waterlogged);
    }
}
