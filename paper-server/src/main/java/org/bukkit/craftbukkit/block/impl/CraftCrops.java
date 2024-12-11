/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCrops extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Ageable {

    public CraftCrops() {
        super();
    }

    public CraftCrops(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftAgeable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty AGE = getInteger(net.minecraft.world.level.block.CropBlock.class, "age");

    @Override
    public int getAge() {
        return this.get(CraftCrops.AGE);
    }

    @Override
    public void setAge(int age) {
        this.set(CraftCrops.AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return getMax(CraftCrops.AGE);
    }
}
