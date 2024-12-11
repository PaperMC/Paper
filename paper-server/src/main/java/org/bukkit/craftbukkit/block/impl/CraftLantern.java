/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftLantern extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Lantern, org.bukkit.block.data.Hangable, org.bukkit.block.data.Waterlogged {

    public CraftLantern() {
        super();
    }

    public CraftLantern(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftHangable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty HANGING = getBoolean(net.minecraft.world.level.block.LanternBlock.class, "hanging");

    @Override
    public boolean isHanging() {
        return this.get(CraftLantern.HANGING);
    }

    @Override
    public void setHanging(boolean hanging) {
        this.set(CraftLantern.HANGING, hanging);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.LanternBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftLantern.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftLantern.WATERLOGGED, waterlogged);
    }
}
