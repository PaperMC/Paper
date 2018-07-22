/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCoralFanAbstract extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Waterlogged {

    public CraftCoralFanAbstract() {
        super();
    }

    public CraftCoralFanAbstract(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.server.BlockStateBoolean WATERLOGGED = getBoolean(net.minecraft.server.BlockCoralFanAbstract.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
