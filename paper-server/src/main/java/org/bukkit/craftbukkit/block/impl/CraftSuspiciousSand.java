/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSuspiciousSand extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.SuspiciousSand {

    public CraftSuspiciousSand() {
        super();
    }

    public CraftSuspiciousSand(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSuspiciousSand

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger DUSTED = getInteger(net.minecraft.world.level.block.SuspiciousSandBlock.class, "dusted");

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
