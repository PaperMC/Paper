/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

import org.bukkit.block.data.type.PinkPetals;

public final class CraftFlowerBed extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.FlowerBed, PinkPetals, org.bukkit.block.data.Directional {

    public CraftFlowerBed() {
        super();
    }

    public CraftFlowerBed(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftPinkPetals

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty FLOWER_AMOUNT = getInteger(net.minecraft.world.level.block.FlowerBedBlock.class, "flower_amount");

    @Override
    public int getFlowerAmount() {
        return this.get(CraftFlowerBed.FLOWER_AMOUNT);
    }

    @Override
    public void setFlowerAmount(int flower_amount) {
        this.set(CraftFlowerBed.FLOWER_AMOUNT, flower_amount);
    }

    // Paper start
    @Override
    public int getMinimumFlowerAmount() {
        return getMin(CraftFlowerBed.FLOWER_AMOUNT);
    }
    // Paper end

    @Override
    public int getMaximumFlowerAmount() {
        return getMax(CraftFlowerBed.FLOWER_AMOUNT);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.FlowerBedBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftFlowerBed.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftFlowerBed.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftFlowerBed.FACING, org.bukkit.block.BlockFace.class);
    }
}
