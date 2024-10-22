/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCreakingHeart extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.CreakingHeart, org.bukkit.block.data.Orientable {

    public CraftCreakingHeart() {
        super();
    }

    public CraftCreakingHeart(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCreakingHeart

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> CREAKING = getEnum(net.minecraft.world.level.block.CreakingHeartBlock.class, "creaking");

    @Override
    public org.bukkit.block.data.type.CreakingHeart.Creaking getCreaking() {
        return get(CREAKING, org.bukkit.block.data.type.CreakingHeart.Creaking.class);
    }

    @Override
    public void setCreaking(org.bukkit.block.data.type.CreakingHeart.Creaking creaking) {
        set(CREAKING, creaking);
    }

    // org.bukkit.craftbukkit.block.data.CraftOrientable

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> AXIS = getEnum(net.minecraft.world.level.block.CreakingHeartBlock.class, "axis");

    @Override
    public org.bukkit.Axis getAxis() {
        return get(AXIS, org.bukkit.Axis.class);
    }

    @Override
    public void setAxis(org.bukkit.Axis axis) {
        set(AXIS, axis);
    }

    @Override
    public java.util.Set<org.bukkit.Axis> getAxes() {
        return getValues(AXIS, org.bukkit.Axis.class);
    }
}
