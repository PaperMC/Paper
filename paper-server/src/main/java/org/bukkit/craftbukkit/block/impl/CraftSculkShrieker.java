/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSculkShrieker extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.SculkShrieker, org.bukkit.block.data.Waterlogged {

    public CraftSculkShrieker() {
        super();
    }

    public CraftSculkShrieker(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSculkShrieker

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean CAN_SUMMON = getBoolean(net.minecraft.world.level.block.SculkShriekerBlock.class, "can_summon");
    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean SHRIEKING = getBoolean(net.minecraft.world.level.block.SculkShriekerBlock.class, "shrieking");

    @Override
    public boolean isCanSummon() {
        return get(CAN_SUMMON);
    }

    @Override
    public void setCanSummon(boolean can_summon) {
        set(CAN_SUMMON, can_summon);
    }

    @Override
    public boolean isShrieking() {
        return get(SHRIEKING);
    }

    @Override
    public void setShrieking(boolean shrieking) {
        set(SHRIEKING, shrieking);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean WATERLOGGED = getBoolean(net.minecraft.world.level.block.SculkShriekerBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
