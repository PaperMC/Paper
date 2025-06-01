package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.util.Vector;

@GeneratedFrom("1.21.6-pre1")
public class CraftCeilingHangingSign extends CraftBlockData implements HangingSign {
    private static final BooleanProperty ATTACHED = CeilingHangingSignBlock.ATTACHED;

    private static final IntegerProperty ROTATION = CeilingHangingSignBlock.ROTATION;

    private static final BooleanProperty WATERLOGGED = CeilingHangingSignBlock.WATERLOGGED;

    public CraftCeilingHangingSign(BlockState state) {
        super(state);
    }

    @Override
    public boolean isAttached() {
        return this.get(ATTACHED);
    }

    @Override
    public void setAttached(final boolean attached) {
        this.set(ATTACHED, attached);
    }

    @Override
    public BlockFace getRotation() {
        return CraftBlockData.ROTATION_CYCLE[this.get(ROTATION)];
    }

    @Override
    public void setRotation(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace != BlockFace.SELF && blockFace.getModY() == 0, "Invalid face, only horizontal face are allowed for this property!");
        Vector dir = blockFace.getDirection();
        float angle = (float) -Math.toDegrees(Math.atan2(dir.getX(), dir.getZ()));
        this.set(ROTATION, RotationSegment.convertToSegment(angle));
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
