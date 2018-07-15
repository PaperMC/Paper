/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTallPlantShearable extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Bisected {

    public CraftTallPlantShearable() {
        super();
    }

    public CraftTallPlantShearable(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftBisected

    private static final net.minecraft.server.BlockStateEnum<?> HALF = getEnum(net.minecraft.server.BlockTallPlantShearable.class, "half");

    @Override
    public Half getHalf() {
        return get(HALF, Half.class);
    }

    @Override
    public void setHalf(Half half) {
        set(HALF, half);
    }
}
