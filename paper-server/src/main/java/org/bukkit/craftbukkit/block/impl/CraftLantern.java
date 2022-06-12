/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftLantern extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Lantern, org.bukkit.block.data.Hangable, org.bukkit.block.data.Waterlogged {

    public CraftLantern() {
        super();
    }

    public CraftLantern(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftHangable

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean HANGING = getBoolean(net.minecraft.world.level.block.BlockLantern.class, "hanging");

    @Override
    public boolean isHanging() {
        return get(HANGING);
    }

    @Override
    public void setHanging(boolean hanging) {
        set(HANGING, hanging);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean WATERLOGGED = getBoolean(net.minecraft.world.level.block.BlockLantern.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
