/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftBarrel extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Barrel, org.bukkit.block.data.Directional, org.bukkit.block.data.Openable {

    public CraftBarrel() {
        super();
    }

    public CraftBarrel(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.BarrelBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftBarrel.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftBarrel.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftBarrel.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftOpenable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty OPEN = getBoolean(net.minecraft.world.level.block.BarrelBlock.class, "open");

    @Override
    public boolean isOpen() {
        return this.get(CraftBarrel.OPEN);
    }

    @Override
    public void setOpen(boolean open) {
        this.set(CraftBarrel.OPEN, open);
    }
}
