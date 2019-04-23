/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftComposter extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Levelled {

    public CraftComposter() {
        super();
    }

    public CraftComposter(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftLevelled

    private static final net.minecraft.server.BlockStateInteger LEVEL = getInteger(net.minecraft.server.BlockComposter.class, "level");

    @Override
    public int getLevel() {
        return get(LEVEL);
    }

    @Override
    public void setLevel(int level) {
        set(LEVEL, level);
    }

    @Override
    public int getMaximumLevel() {
        return getMax(LEVEL);
    }
}
