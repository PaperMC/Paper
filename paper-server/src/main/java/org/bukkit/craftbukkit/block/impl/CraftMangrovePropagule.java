/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftMangrovePropagule extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.MangrovePropagule, org.bukkit.block.data.Ageable, org.bukkit.block.data.Hangable, org.bukkit.block.data.type.Sapling, org.bukkit.block.data.Waterlogged {

    public CraftMangrovePropagule() {
        super();
    }

    public CraftMangrovePropagule(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftAgeable

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger AGE = getInteger(net.minecraft.world.level.block.MangrovePropaguleBlock.class, "age");

    @Override
    public int getAge() {
        return get(AGE);
    }

    @Override
    public void setAge(int age) {
        set(AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return getMax(AGE);
    }

    // org.bukkit.craftbukkit.block.data.CraftHangable

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean HANGING = getBoolean(net.minecraft.world.level.block.MangrovePropaguleBlock.class, "hanging");

    @Override
    public boolean isHanging() {
        return get(HANGING);
    }

    @Override
    public void setHanging(boolean hanging) {
        set(HANGING, hanging);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSapling

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger STAGE = getInteger(net.minecraft.world.level.block.MangrovePropaguleBlock.class, "stage");

    @Override
    public int getStage() {
        return get(STAGE);
    }

    @Override
    public void setStage(int stage) {
        set(STAGE, stage);
    }

    @Override
    public int getMaximumStage() {
        return getMax(STAGE);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean WATERLOGGED = getBoolean(net.minecraft.world.level.block.MangrovePropaguleBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
