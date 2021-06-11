/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCaveVinesPlant extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.CaveVinesPlant {

    public CraftCaveVinesPlant() {
        super();
    }

    public CraftCaveVinesPlant(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCaveVinesPlant

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean BERRIES = getBoolean(net.minecraft.world.level.block.CaveVinesPlantBlock.class, "berries");

    @Override
    public boolean isBerries() {
        return get(BERRIES);
    }

    @Override
    public void setBerries(boolean berries) {
        set(BERRIES, berries);
    }
}
