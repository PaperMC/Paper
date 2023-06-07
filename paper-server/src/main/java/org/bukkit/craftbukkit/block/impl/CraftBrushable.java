/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftBrushable extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Brushable {

    public CraftBrushable() {
        super();
    }

    public CraftBrushable(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftBrushable

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger DUSTED = getInteger(net.minecraft.world.level.block.BrushableBlock.class, "dusted");

    @Override
    public int getDusted() {
        return get(DUSTED);
    }

    @Override
    public void setDusted(int dusted) {
        set(DUSTED, dusted);
    }

    @Override
    public int getMaximumDusted() {
        return getMax(DUSTED);
    }
}
