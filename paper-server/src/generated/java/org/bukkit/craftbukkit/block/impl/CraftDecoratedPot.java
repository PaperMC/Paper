package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.DecoratedPot;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftDecoratedPot extends CraftBlockData implements DecoratedPot {
    private static final BooleanProperty CRACKED = DecoratedPotBlock.CRACKED;

    private static final EnumProperty<Direction> HORIZONTAL_FACING = DecoratedPotBlock.HORIZONTAL_FACING;

    private static final BooleanProperty WATERLOGGED = DecoratedPotBlock.WATERLOGGED;

    public CraftDecoratedPot(BlockState state) {
        super(state);
    }

    @Override
    public boolean isCracked() {
        return this.get(CRACKED);
    }

    @Override
    public void setCracked(final boolean cracked) {
        this.set(CRACKED, cracked);
    }

    @Override
    public BlockFace getFacing() {
        return this.get(HORIZONTAL_FACING, BlockFace.class);
    }

    @Override
    public void setFacing(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace.isCartesian() && blockFace.getModY() == 0, "Invalid face, only cartesian horizontal face are allowed for this property!");
        this.set(HORIZONTAL_FACING, blockFace);
    }

    @Override
    public Set<BlockFace> getFaces() {
        return this.getValues(HORIZONTAL_FACING, BlockFace.class);
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
