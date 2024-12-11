/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSculkShrieker extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.SculkShrieker, org.bukkit.block.data.Waterlogged {

    public CraftSculkShrieker() {
        super();
    }

    public CraftSculkShrieker(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSculkShrieker

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty CAN_SUMMON = getBoolean(net.minecraft.world.level.block.SculkShriekerBlock.class, "can_summon");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty SHRIEKING = getBoolean(net.minecraft.world.level.block.SculkShriekerBlock.class, "shrieking");

    @Override
    public boolean isCanSummon() {
        return this.get(CraftSculkShrieker.CAN_SUMMON);
    }

    @Override
    public void setCanSummon(boolean can_summon) {
        this.set(CraftSculkShrieker.CAN_SUMMON, can_summon);
    }

    @Override
    public boolean isShrieking() {
        return this.get(CraftSculkShrieker.SHRIEKING);
    }

    @Override
    public void setShrieking(boolean shrieking) {
        this.set(CraftSculkShrieker.SHRIEKING, shrieking);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.SculkShriekerBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftSculkShrieker.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftSculkShrieker.WATERLOGGED, waterlogged);
    }
}
