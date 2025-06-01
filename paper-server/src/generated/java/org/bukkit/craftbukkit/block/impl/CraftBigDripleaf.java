package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BigDripleafBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.BigDripleaf;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftBigDripleaf extends CraftBlockData implements BigDripleaf {
    private static final EnumProperty<Direction> FACING = BigDripleafBlock.FACING;

    private static final EnumProperty<net.minecraft.world.level.block.state.properties.Tilt> TILT = BlockStateProperties.TILT;

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public CraftBigDripleaf(BlockState state) {
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
    public BigDripleaf.Tilt getTilt() {
        return this.get(TILT, BigDripleaf.Tilt.class);
    }

    @Override
    public void setTilt(final BigDripleaf.Tilt tilt) {
        Preconditions.checkArgument(tilt != null, "tilt cannot be null!");
        this.set(TILT, tilt);
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
