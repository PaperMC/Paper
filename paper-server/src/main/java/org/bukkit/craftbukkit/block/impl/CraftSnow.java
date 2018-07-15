/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSnow extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Snow {

    public CraftSnow() {
        super();
    }

    public CraftSnow(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSnow

    private static final net.minecraft.server.BlockStateInteger LAYERS = getInteger(net.minecraft.server.BlockSnow.class, "layers");

    @Override
    public int getLayers() {
        return get(LAYERS);
    }

    @Override
    public void setLayers(int layers) {
        set(LAYERS, layers);
    }

    @Override
    public int getMinimumLayers() {
        return getMin(LAYERS);
    }

    @Override
    public int getMaximumLayers() {
        return getMax(LAYERS);
    }
}
