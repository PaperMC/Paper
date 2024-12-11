/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftGrass extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Snowable {

    public CraftGrass() {
        super();
    }

    public CraftGrass(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftSnowable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty SNOWY = getBoolean(net.minecraft.world.level.block.GrassBlock.class, "snowy");

    @Override
    public boolean isSnowy() {
        return this.get(CraftGrass.SNOWY);
    }

    @Override
    public void setSnowy(boolean snowy) {
        this.set(CraftGrass.SNOWY, snowy);
    }
}
