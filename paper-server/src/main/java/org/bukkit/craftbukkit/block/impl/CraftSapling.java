/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSapling extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Sapling {

    public CraftSapling() {
        super();
    }

    public CraftSapling(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSapling

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty STAGE = getInteger(net.minecraft.world.level.block.SaplingBlock.class, "stage");

    @Override
    public int getStage() {
        return this.get(CraftSapling.STAGE);
    }

    @Override
    public void setStage(int stage) {
        this.set(CraftSapling.STAGE, stage);
    }

    @Override
    public int getMaximumStage() {
        return getMax(CraftSapling.STAGE);
    }
}
