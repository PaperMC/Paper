package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.annotation.GeneratedClass;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SideChainPart;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Shelf;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftShelf extends CraftBlockData implements Shelf {
    private static final EnumProperty<Direction> FACING = ShelfBlock.FACING;

    private static final BooleanProperty POWERED = ShelfBlock.POWERED;

    private static final EnumProperty<SideChainPart> SIDE_CHAIN_PART = ShelfBlock.SIDE_CHAIN_PART;

    private static final BooleanProperty WATERLOGGED = ShelfBlock.WATERLOGGED;

    public CraftShelf(BlockState state) {
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
    public boolean isPowered() {
        return this.get(POWERED);
    }

    @Override
    public void setPowered(final boolean powered) {
        this.set(POWERED, powered);
    }

    @Override
    public org.bukkit.block.data.SideChaining.ChainPart getSideChain() {
        return this.get(SIDE_CHAIN_PART, org.bukkit.block.data.SideChaining.ChainPart.class);
    }

    @Override
    public void setSideChain(final org.bukkit.block.data.SideChaining.ChainPart chainPart) {
        Preconditions.checkArgument(chainPart != null, "chainPart cannot be null!");
        this.set(SIDE_CHAIN_PART, chainPart);
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
