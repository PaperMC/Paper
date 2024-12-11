/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftPiston extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Piston, org.bukkit.block.data.Directional {

    public CraftPiston() {
        super();
    }

    public CraftPiston(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftPiston

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty EXTENDED = getBoolean(net.minecraft.world.level.block.piston.PistonBaseBlock.class, "extended");

    @Override
    public boolean isExtended() {
        return this.get(CraftPiston.EXTENDED);
    }

    @Override
    public void setExtended(boolean extended) {
        this.set(CraftPiston.EXTENDED, extended);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.piston.PistonBaseBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftPiston.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftPiston.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftPiston.FACING, org.bukkit.block.BlockFace.class);
    }
}
