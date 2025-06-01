package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.bukkit.block.data.Rail;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftRail extends CraftBlockData implements Rail {
    private static final EnumProperty<RailShape> SHAPE = RailBlock.SHAPE;

    private static final BooleanProperty WATERLOGGED = RailBlock.WATERLOGGED;

    public CraftRail(BlockState state) {
        super(state);
    }

    @Override
    public Rail.Shape getShape() {
        return this.get(SHAPE, Rail.Shape.class);
    }

    @Override
    public void setShape(final Rail.Shape shape) {
        Preconditions.checkArgument(shape != null, "shape cannot be null!");
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
