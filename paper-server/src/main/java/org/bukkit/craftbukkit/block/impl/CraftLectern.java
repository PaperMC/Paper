/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftLectern extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Lectern, org.bukkit.block.data.Directional, org.bukkit.block.data.Powerable {

    public CraftLectern() {
        super();
    }

    public CraftLectern(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftLectern

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty HAS_BOOK = getBoolean(net.minecraft.world.level.block.LecternBlock.class, "has_book");

    @Override
    public boolean hasBook() {
        return this.get(CraftLectern.HAS_BOOK);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.LecternBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftLectern.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftLectern.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftLectern.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.LecternBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftLectern.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftLectern.POWERED, powered);
    }
}
