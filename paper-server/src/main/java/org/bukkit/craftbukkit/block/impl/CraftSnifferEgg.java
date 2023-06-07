/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSnifferEgg extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Hatchable {

    public CraftSnifferEgg() {
        super();
    }

    public CraftSnifferEgg(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftHatchable

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger HATCH = getInteger(net.minecraft.world.level.block.SnifferEggBlock.class, "hatch");

    @Override
    public int getHatch() {
        return get(HATCH);
    }

    @Override
    public void setHatch(int hatch) {
        set(HATCH, hatch);
    }

    @Override
    public int getMaximumHatch() {
        return getMax(HATCH);
    }
}
