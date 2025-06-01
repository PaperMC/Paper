package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Door;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftDoor extends CraftBlockData implements Door {
    private static final EnumProperty<Direction> FACING = DoorBlock.FACING;

    private static final EnumProperty<DoubleBlockHalf> HALF = DoorBlock.HALF;

    private static final EnumProperty<DoorHingeSide> HINGE = DoorBlock.HINGE;

    private static final BooleanProperty OPEN = DoorBlock.OPEN;

    private static final BooleanProperty POWERED = DoorBlock.POWERED;

    public CraftDoor(BlockState state) {
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
    public Door.Hinge getHinge() {
        return this.get(HINGE, Door.Hinge.class);
    }

    @Override
    public void setHinge(final Door.Hinge hinge) {
        Preconditions.checkArgument(hinge != null, "hinge cannot be null!");
        this.set(HINGE, hinge);
    }

    @Override
    public boolean isOpen() {
        return this.get(OPEN);
    }

    @Override
    public void setOpen(final boolean open) {
        this.set(OPEN, open);
    }

    @Override
    public boolean isPowered() {
        return this.get(POWERED);
    }

    @Override
    public void setPowered(final boolean powered) {
        this.set(POWERED, powered);
    }
}
