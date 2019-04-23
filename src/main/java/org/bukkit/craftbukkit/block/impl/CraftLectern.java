/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftLectern extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Lectern, org.bukkit.block.data.Directional, org.bukkit.block.data.Powerable {

    public CraftLectern() {
        super();
    }

    public CraftLectern(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftLectern

    private static final net.minecraft.server.BlockStateBoolean HAS_BOOK = getBoolean(net.minecraft.server.BlockLectern.class, "has_book");

    @Override
    public boolean hasBook() {
        return get(HAS_BOOK);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.server.BlockStateEnum<?> FACING = getEnum(net.minecraft.server.BlockLectern.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return get(FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        set(FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return getValues(FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.server.BlockStateBoolean POWERED = getBoolean(net.minecraft.server.BlockLectern.class, "powered");

    @Override
    public boolean isPowered() {
        return get(POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        set(POWERED, powered);
    }
}
