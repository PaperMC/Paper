/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftStonecutter extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Directional {

    public CraftStonecutter() {
        super();
    }

    public CraftStonecutter(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.StonecutterBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftStonecutter.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftStonecutter.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftStonecutter.FACING, org.bukkit.block.BlockFace.class);
    }
}
