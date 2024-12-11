/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTarget extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.AnaloguePowerable {

    public CraftTarget() {
        super();
    }

    public CraftTarget(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftAnaloguePowerable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty POWER = getInteger(net.minecraft.world.level.block.TargetBlock.class, "power");

    @Override
    public int getPower() {
        return this.get(CraftTarget.POWER);
    }

    @Override
    public void setPower(int power) {
        this.set(CraftTarget.POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return getMax(CraftTarget.POWER);
    }
}
