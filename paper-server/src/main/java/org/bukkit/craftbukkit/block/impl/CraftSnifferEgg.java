/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSnifferEgg extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Hatchable {

    public CraftSnifferEgg() {
        super();
    }

    public CraftSnifferEgg(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftHatchable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty HATCH = getInteger(net.minecraft.world.level.block.SnifferEggBlock.class, "hatch");

    @Override
    public int getHatch() {
        return this.get(CraftSnifferEgg.HATCH);
    }

    @Override
    public void setHatch(int hatch) {
        this.set(CraftSnifferEgg.HATCH, hatch);
    }

    @Override
    public int getMaximumHatch() {
        return getMax(CraftSnifferEgg.HATCH);
    }
}
