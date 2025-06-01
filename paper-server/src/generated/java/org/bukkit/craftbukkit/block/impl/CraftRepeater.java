package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftRepeater extends CraftBlockData implements Repeater {
    private static final IntegerProperty DELAY = RepeaterBlock.DELAY;

    private static final EnumProperty<Direction> FACING = RepeaterBlock.FACING;

    private static final BooleanProperty LOCKED = RepeaterBlock.LOCKED;

    private static final BooleanProperty POWERED = RepeaterBlock.POWERED;

    public CraftRepeater(BlockState state) {
        super(state);
    }

    @Override
    public int getDelay() {
        return this.get(DELAY);
    }

    @Override
    public void setDelay(final int delay) {
        this.set(DELAY, delay);
    }

    @Override
    public int getMinimumDelay() {
        return DELAY.min;
    }

    @Override
    public int getMaximumDelay() {
        return DELAY.max;
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
    public boolean isLocked() {
        return this.get(LOCKED);
    }

    @Override
    public void setLocked(final boolean locked) {
        this.set(LOCKED, locked);
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
