/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftComposter extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Levelled {

    public CraftComposter() {
        super();
    }

    public CraftComposter(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftLevelled

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty LEVEL = getInteger(net.minecraft.world.level.block.ComposterBlock.class, "level");

    @Override
    public int getLevel() {
        return this.get(CraftComposter.LEVEL);
    }

    @Override
    public void setLevel(int level) {
        this.set(CraftComposter.LEVEL, level);
    }

    @Override
    public int getMaximumLevel() {
        return getMax(CraftComposter.LEVEL);
    }

    // Paper start
    @Override
    public int getMinimumLevel() {
        return getMin(CraftComposter.LEVEL);
    }
    // Paper end
}
