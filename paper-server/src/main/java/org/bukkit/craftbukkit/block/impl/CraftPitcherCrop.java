/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftPitcherCrop extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.PitcherCrop, org.bukkit.block.data.Ageable, org.bukkit.block.data.Bisected {

    public CraftPitcherCrop() {
        super();
    }

    public CraftPitcherCrop(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftAgeable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty AGE = getInteger(net.minecraft.world.level.block.PitcherCropBlock.class, "age");

    @Override
    public int getAge() {
        return this.get(CraftPitcherCrop.AGE);
    }

    @Override
    public void setAge(int age) {
        this.set(CraftPitcherCrop.AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return getMax(CraftPitcherCrop.AGE);
    }

    // org.bukkit.craftbukkit.block.data.CraftBisected

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> HALF = getEnum(net.minecraft.world.level.block.PitcherCropBlock.class, "half");

    @Override
    public org.bukkit.block.data.Bisected.Half getHalf() {
        return this.get(CraftPitcherCrop.HALF, org.bukkit.block.data.Bisected.Half.class);
    }

    @Override
    public void setHalf(org.bukkit.block.data.Bisected.Half half) {
        this.set(CraftPitcherCrop.HALF, half);
    }
}
