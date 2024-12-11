/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftMycel extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Snowable {

    public CraftMycel() {
        super();
    }

    public CraftMycel(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftSnowable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty SNOWY = getBoolean(net.minecraft.world.level.block.MyceliumBlock.class, "snowy");

    @Override
    public boolean isSnowy() {
        return this.get(CraftMycel.SNOWY);
    }

    @Override
    public void setSnowy(boolean snowy) {
        this.set(CraftMycel.SNOWY, snowy);
    }
}
