/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftPortal extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Orientable {

    public CraftPortal() {
        super();
    }

    public CraftPortal(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftOrientable

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> AXIS = getEnum(net.minecraft.world.level.block.NetherPortalBlock.class, "axis");

    @Override
    public org.bukkit.Axis getAxis() {
        return this.get(CraftPortal.AXIS, org.bukkit.Axis.class);
    }

    @Override
    public void setAxis(org.bukkit.Axis axis) {
        this.set(CraftPortal.AXIS, axis);
    }

    @Override
    public java.util.Set<org.bukkit.Axis> getAxes() {
        return this.getValues(CraftPortal.AXIS, org.bukkit.Axis.class);
    }
}
