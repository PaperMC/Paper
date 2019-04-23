/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCampfire extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Campfire, org.bukkit.block.data.Lightable, org.bukkit.block.data.Waterlogged {

    public CraftCampfire() {
        super();
    }

    public CraftCampfire(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCampfire

    private static final net.minecraft.server.BlockStateBoolean SIGNAL_FIRE = getBoolean(net.minecraft.server.BlockCampfire.class, "signal_fire");

    @Override
    public boolean isSignalFire() {
        return get(SIGNAL_FIRE);
    }

    @Override
    public void setSignalFire(boolean signalFire) {
        set(SIGNAL_FIRE, signalFire);
    }

    // org.bukkit.craftbukkit.block.data.CraftLightable

    private static final net.minecraft.server.BlockStateBoolean LIT = getBoolean(net.minecraft.server.BlockCampfire.class, "lit");

    @Override
    public boolean isLit() {
        return get(LIT);
    }

    @Override
    public void setLit(boolean lit) {
        set(LIT, lit);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.server.BlockStateBoolean WATERLOGGED = getBoolean(net.minecraft.server.BlockCampfire.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
