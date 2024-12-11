package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Orientable;

public class CraftOrientable extends CraftBlockData implements Orientable {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> AXIS = getEnum("axis");

    @Override
    public org.bukkit.Axis getAxis() {
        return this.get(CraftOrientable.AXIS, org.bukkit.Axis.class);
    }

    @Override
    public void setAxis(org.bukkit.Axis axis) {
        this.set(CraftOrientable.AXIS, axis);
    }

    @Override
    public java.util.Set<org.bukkit.Axis> getAxes() {
        return this.getValues(CraftOrientable.AXIS, org.bukkit.Axis.class);
    }
}
