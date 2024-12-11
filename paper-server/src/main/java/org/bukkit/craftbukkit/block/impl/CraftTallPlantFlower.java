/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTallPlantFlower extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Bisected {

    public CraftTallPlantFlower() {
        super();
    }

    public CraftTallPlantFlower(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftBisected

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> HALF = getEnum(net.minecraft.world.level.block.TallFlowerBlock.class, "half");

    @Override
    public org.bukkit.block.data.Bisected.Half getHalf() {
        return this.get(CraftTallPlantFlower.HALF, org.bukkit.block.data.Bisected.Half.class);
    }

    @Override
    public void setHalf(org.bukkit.block.data.Bisected.Half half) {
        this.set(CraftTallPlantFlower.HALF, half);
    }
}
