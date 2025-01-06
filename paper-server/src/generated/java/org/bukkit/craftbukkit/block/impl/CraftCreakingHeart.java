package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.Axis;
import org.bukkit.block.data.type.CreakingHeart;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.4")
public class CraftCreakingHeart extends CraftBlockData implements CreakingHeart {
    private static final BooleanProperty ACTIVE = CreakingHeartBlock.ACTIVE;

    private static final EnumProperty<Direction.Axis> AXIS = CreakingHeartBlock.AXIS;

    private static final BooleanProperty NATURAL = CreakingHeartBlock.NATURAL;

    public CraftCreakingHeart(BlockState state) {
        super(state);
    }

    @Override
    public boolean isActive() {
        return this.get(ACTIVE);
    }

    @Override
    public void setActive(final boolean active) {
        this.set(ACTIVE, active);
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

    @Override
    public boolean isNatural() {
        return this.get(NATURAL);
    }

    @Override
    public void setNatural(final boolean natural) {
        this.set(NATURAL, natural);
    }
}
