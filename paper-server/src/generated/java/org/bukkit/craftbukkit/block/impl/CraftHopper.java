package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftHopper extends CraftBlockData implements Hopper {
    private static final BooleanProperty ENABLED = HopperBlock.ENABLED;

    private static final EnumProperty<Direction> FACING = HopperBlock.FACING;

    public CraftHopper(BlockState state) {
        super(state);
    }

    @Override
    public boolean isEnabled() {
        return this.get(ENABLED);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.set(ENABLED, enabled);
    }

    @Override
    public BlockFace getFacing() {
        return this.get(FACING, BlockFace.class);
    }

    @Override
    public void setFacing(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace.isCartesian() && blockFace != BlockFace.UP, "Invalid face, only cartesian face (excluding UP) are allowed for this property!");
        this.set(FACING, blockFace);
    }

    @Override
    public Set<BlockFace> getFaces() {
        return this.getValues(FACING, BlockFace.class);
    }
}
