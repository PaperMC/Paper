/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftHopper extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Hopper, org.bukkit.block.data.Directional {

    public CraftHopper() {
        super();
    }

    public CraftHopper(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftHopper

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty ENABLED = getBoolean(net.minecraft.world.level.block.HopperBlock.class, "enabled");

    @Override
    public boolean isEnabled() {
        return this.get(CraftHopper.ENABLED);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.set(CraftHopper.ENABLED, enabled);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.HopperBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftHopper.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftHopper.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftHopper.FACING, org.bukkit.block.BlockFace.class);
    }
}
