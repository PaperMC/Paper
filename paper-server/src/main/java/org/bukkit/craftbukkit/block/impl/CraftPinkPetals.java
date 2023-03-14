/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftPinkPetals extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.PinkPetals, org.bukkit.block.data.Directional {

    public CraftPinkPetals() {
        super();
    }

    public CraftPinkPetals(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftPinkPetals

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger FLOWER_AMOUNT = getInteger(net.minecraft.world.level.block.PinkPetalsBlock.class, "flower_amount");

    @Override
    public int getFlowerAmount() {
        return get(FLOWER_AMOUNT);
    }

    @Override
    public void setFlowerAmount(int flower_amount) {
        set(FLOWER_AMOUNT, flower_amount);
    }

    @Override
    public int getMaximumFlowerAmount() {
        return getMax(FLOWER_AMOUNT);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> FACING = getEnum(net.minecraft.world.level.block.PinkPetalsBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return get(FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        set(FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return getValues(FACING, org.bukkit.block.BlockFace.class);
    }
}
