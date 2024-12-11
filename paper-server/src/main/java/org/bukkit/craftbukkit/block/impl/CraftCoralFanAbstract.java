/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCoralFanAbstract extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Waterlogged {

    public CraftCoralFanAbstract() {
        super();
    }

    public CraftCoralFanAbstract(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.BaseCoralFanBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftCoralFanAbstract.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftCoralFanAbstract.WATERLOGGED, waterlogged);
    }
}
