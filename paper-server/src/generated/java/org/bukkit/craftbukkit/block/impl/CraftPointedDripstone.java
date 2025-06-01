package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftPointedDripstone extends CraftBlockData implements PointedDripstone {
    private static final EnumProperty<DripstoneThickness> THICKNESS = PointedDripstoneBlock.THICKNESS;

    private static final EnumProperty<Direction> TIP_DIRECTION = PointedDripstoneBlock.TIP_DIRECTION;

    private static final BooleanProperty WATERLOGGED = PointedDripstoneBlock.WATERLOGGED;

    public CraftPointedDripstone(BlockState state) {
        super(state);
    }

    @Override
    public PointedDripstone.Thickness getThickness() {
        return this.get(THICKNESS, PointedDripstone.Thickness.class);
    }

    @Override
    public void setThickness(final PointedDripstone.Thickness thickness) {
        Preconditions.checkArgument(thickness != null, "thickness cannot be null!");
        this.set(THICKNESS, thickness);
    }

    @Override
    public BlockFace getVerticalDirection() {
        return this.get(TIP_DIRECTION, BlockFace.class);
    }

    @Override
    public void setVerticalDirection(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace.getModY() != 0, "Invalid face, only vertical face are allowed for this property!");
        this.set(TIP_DIRECTION, blockFace);
    }

    @Override
    public Set<BlockFace> getVerticalDirections() {
        return this.getValues(TIP_DIRECTION, BlockFace.class);
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
