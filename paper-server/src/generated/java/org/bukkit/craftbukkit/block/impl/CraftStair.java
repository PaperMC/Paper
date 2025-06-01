package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftStair extends CraftBlockData implements Stairs {
    private static final EnumProperty<Direction> FACING = StairBlock.FACING;

    private static final EnumProperty<net.minecraft.world.level.block.state.properties.Half> HALF = StairBlock.HALF;

    private static final EnumProperty<StairsShape> SHAPE = StairBlock.SHAPE;

    private static final BooleanProperty WATERLOGGED = StairBlock.WATERLOGGED;

    public CraftStair(BlockState state) {
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
    public org.bukkit.block.data.Bisected.Half getHalf() {
        return this.get(HALF, org.bukkit.block.data.Bisected.Half.class);
    }

    @Override
    public void setHalf(final org.bukkit.block.data.Bisected.Half half) {
        Preconditions.checkArgument(half != null, "half cannot be null!");
        this.set(HALF, half);
    }

    @Override
    public Stairs.Shape getShape() {
        return this.get(SHAPE, Stairs.Shape.class);
    }

    @Override
    public void setShape(final Stairs.Shape shape) {
        Preconditions.checkArgument(shape != null, "shape cannot be null!");
        this.set(SHAPE, shape);
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
