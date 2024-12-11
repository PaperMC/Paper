/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftPinkPetals extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.PinkPetals, org.bukkit.block.data.Directional {

    public CraftPinkPetals() {
        super();
    }

    public CraftPinkPetals(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftPinkPetals

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty FLOWER_AMOUNT = getInteger(net.minecraft.world.level.block.PinkPetalsBlock.class, "flower_amount");

    @Override
    public int getFlowerAmount() {
        return this.get(CraftPinkPetals.FLOWER_AMOUNT);
    }

    @Override
    public void setFlowerAmount(int flower_amount) {
        this.set(CraftPinkPetals.FLOWER_AMOUNT, flower_amount);
    }

    @Override
    public int getMaximumFlowerAmount() {
        return getMax(CraftPinkPetals.FLOWER_AMOUNT);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.PinkPetalsBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftPinkPetals.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftPinkPetals.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftPinkPetals.FACING, org.bukkit.block.BlockFace.class);
    }
}
