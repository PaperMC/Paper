package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6")
public class CraftPoweredRail extends CraftBlockData implements RedstoneRail {
    private static final BooleanProperty POWERED = PoweredRailBlock.POWERED;

    private static final EnumProperty<RailShape> SHAPE = PoweredRailBlock.SHAPE;

    private static final BooleanProperty WATERLOGGED = PoweredRailBlock.WATERLOGGED;

    public CraftPoweredRail(BlockState state) {
        super(state);
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
    public Rail.Shape getShape() {
        return this.get(SHAPE, Rail.Shape.class);
    }

    @Override
    public void setShape(final Rail.Shape shape) {
        Preconditions.checkArgument(shape != null, "shape cannot be null!");
        Preconditions.checkArgument(shape != Rail.Shape.NORTH_EAST && shape != Rail.Shape.NORTH_WEST && shape != Rail.Shape.SOUTH_EAST && shape != Rail.Shape.SOUTH_WEST, "Invalid rail shape, only straight rail are allowed for this property!");
        this.set(SHAPE, shape);
    }

    @Override
    public Set<Rail.Shape> getShapes() {
        return this.getValues(SHAPE, Rail.Shape.class);
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
