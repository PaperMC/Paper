/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftMangrovePropagule extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.MangrovePropagule, org.bukkit.block.data.Ageable, org.bukkit.block.data.Hangable, org.bukkit.block.data.type.Sapling, org.bukkit.block.data.Waterlogged {

    public CraftMangrovePropagule() {
        super();
    }

    public CraftMangrovePropagule(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftAgeable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty AGE = getInteger(net.minecraft.world.level.block.MangrovePropaguleBlock.class, "age");

    @Override
    public int getAge() {
        return this.get(CraftMangrovePropagule.AGE);
    }

    @Override
    public void setAge(int age) {
        this.set(CraftMangrovePropagule.AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return getMax(CraftMangrovePropagule.AGE);
    }

    // org.bukkit.craftbukkit.block.data.CraftHangable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty HANGING = getBoolean(net.minecraft.world.level.block.MangrovePropaguleBlock.class, "hanging");

    @Override
    public boolean isHanging() {
        return this.get(CraftMangrovePropagule.HANGING);
    }

    @Override
    public void setHanging(boolean hanging) {
        this.set(CraftMangrovePropagule.HANGING, hanging);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSapling

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty STAGE = getInteger(net.minecraft.world.level.block.MangrovePropaguleBlock.class, "stage");

    @Override
    public int getStage() {
        return this.get(CraftMangrovePropagule.STAGE);
    }

    @Override
    public void setStage(int stage) {
        this.set(CraftMangrovePropagule.STAGE, stage);
    }

    @Override
    public int getMaximumStage() {
        return getMax(CraftMangrovePropagule.STAGE);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.MangrovePropaguleBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftMangrovePropagule.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftMangrovePropagule.WATERLOGGED, waterlogged);
    }
}
