package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DriedGhastBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.DriedGhast;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftDriedGhast extends CraftBlockData implements DriedGhast {
    private static final EnumProperty<Direction> FACING = DriedGhastBlock.FACING;

    private static final IntegerProperty HYDRATION_LEVEL = DriedGhastBlock.HYDRATION_LEVEL;

    private static final BooleanProperty WATERLOGGED = DriedGhastBlock.WATERLOGGED;

    public CraftDriedGhast(BlockState state) {
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

    public int getHydration() {
        return this.get(HYDRATION_LEVEL);
    }

    public void setHydration(final int hydration) {
        this.set(HYDRATION_LEVEL, hydration);
    }

    public int getMaximumHydration() {
        return HYDRATION_LEVEL.max;
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
