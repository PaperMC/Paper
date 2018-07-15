/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCake extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Cake {

    public CraftCake() {
        super();
    }

    public CraftCake(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCake

    private static final net.minecraft.server.BlockStateInteger BITES = getInteger(net.minecraft.server.BlockCake.class, "bites");

    @Override
    public int getBites() {
        return get(BITES);
    }

    @Override
    public void setBites(int bites) {
        set(BITES, bites);
    }

    @Override
    public int getMaximumBites() {
        return getMax(BITES);
    }
}
