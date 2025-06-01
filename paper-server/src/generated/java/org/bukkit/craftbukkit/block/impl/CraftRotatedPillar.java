package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.Axis;
import org.bukkit.block.data.Orientable;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftRotatedPillar extends CraftBlockData implements Orientable {
    private static final EnumProperty<Direction.Axis> AXIS = RotatedPillarBlock.AXIS;

    public CraftRotatedPillar(BlockState state) {
        super(state);
    }

    @Override
    public Axis getAxis() {
        return this.get(AXIS, Axis.class);
    }

    @Override
    public void setAxis(final Axis axis) {
        Preconditions.checkArgument(axis != null, "axis cannot be null!");
        this.set(AXIS, axis);
    }

    @Override
    public Set<Axis> getAxes() {
        return this.getValues(AXIS, Axis.class);
    }
}
