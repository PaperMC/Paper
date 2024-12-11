/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftFluids extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Levelled {

    public CraftFluids() {
        super();
    }

    public CraftFluids(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftLevelled

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty LEVEL = getInteger(net.minecraft.world.level.block.LiquidBlock.class, "level");

    @Override
    public int getLevel() {
        return this.get(CraftFluids.LEVEL);
    }

    @Override
    public void setLevel(int level) {
        this.set(CraftFluids.LEVEL, level);
    }

    @Override
    public int getMaximumLevel() {
        return getMax(CraftFluids.LEVEL);
    }

    // Paper start
    @Override
    public int getMinimumLevel() {
        return getMin(CraftFluids.LEVEL);
    }
    // Paper end
}
