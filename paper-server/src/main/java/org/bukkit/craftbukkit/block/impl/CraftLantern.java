/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftLantern extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Lantern, org.bukkit.block.data.Waterlogged {

    public CraftLantern() {
        super();
    }

    public CraftLantern(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftLantern

    private static final net.minecraft.server.BlockStateBoolean HANGING = getBoolean(net.minecraft.server.BlockLantern.class, "hanging");

    @Override
    public boolean isHanging() {
        return get(HANGING);
    }

    @Override
    public void setHanging(boolean hanging) {
        set(HANGING, hanging);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.server.BlockStateBoolean WATERLOGGED = getBoolean(net.minecraft.server.BlockLantern.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
