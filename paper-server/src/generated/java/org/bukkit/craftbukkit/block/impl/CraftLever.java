package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Switch;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.5")
public class CraftLever extends CraftBlockData implements Switch {
    private static final EnumProperty<AttachFace> FACE = LeverBlock.FACE;

    private static final EnumProperty<Direction> FACING = LeverBlock.FACING;

    private static final BooleanProperty POWERED = LeverBlock.POWERED;

    public CraftLever(BlockState state) {
        super(state);
    }

    @Override
    public org.bukkit.block.data.FaceAttachable.AttachedFace getAttachedFace() {
        return this.get(FACE, org.bukkit.block.data.FaceAttachable.AttachedFace.class);
    }

    @Override
    public void setAttachedFace(
            final org.bukkit.block.data.FaceAttachable.AttachedFace attachedFace) {
        Preconditions.checkArgument(attachedFace != null, "attachedFace cannot be null!");
        this.set(FACE, attachedFace);
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
}
