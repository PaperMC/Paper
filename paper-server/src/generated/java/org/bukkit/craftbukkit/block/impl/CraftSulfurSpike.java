package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.annotation.GeneratedClass;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SulfurSpikeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SpeleothemThickness;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Speleothem;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftSulfurSpike extends CraftBlockData implements Speleothem {
    private static final EnumProperty<SpeleothemThickness> THICKNESS = SulfurSpikeBlock.THICKNESS;

    private static final EnumProperty<Direction> TIP_DIRECTION = SulfurSpikeBlock.TIP_DIRECTION;

    private static final BooleanProperty WATERLOGGED = SulfurSpikeBlock.WATERLOGGED;

    public CraftSulfurSpike(BlockState state) {
        super(state);
    }

    @Override
    public Speleothem.Thickness getThickness() {
        return this.get(THICKNESS, Speleothem.Thickness.class);
    }

    @Override
    public void setThickness(final Speleothem.Thickness thickness) {
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
