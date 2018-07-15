/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftDaylightDetector extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.DaylightDetector, org.bukkit.block.data.AnaloguePowerable {

    public CraftDaylightDetector() {
        super();
    }

    public CraftDaylightDetector(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftDaylightDetector

    private static final net.minecraft.server.BlockStateBoolean INVERTED = getBoolean(net.minecraft.server.BlockDaylightDetector.class, "inverted");

    @Override
    public boolean isInverted() {
        return get(INVERTED);
    }

    @Override
    public void setInverted(boolean inverted) {
        set(INVERTED, inverted);
    }

    // org.bukkit.craftbukkit.block.data.CraftAnaloguePowerable

    private static final net.minecraft.server.BlockStateInteger POWER = getInteger(net.minecraft.server.BlockDaylightDetector.class, "power");

    @Override
    public int getPower() {
        return get(POWER);
    }

    @Override
    public void setPower(int power) {
        set(POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return getMax(POWER);
    }
}
