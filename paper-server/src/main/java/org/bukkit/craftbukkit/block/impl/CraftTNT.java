/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTNT extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.TNT {

    public CraftTNT() {
        super();
    }

    public CraftTNT(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftTNT

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty UNSTABLE = getBoolean(net.minecraft.world.level.block.TntBlock.class, "unstable");

    @Override
    public boolean isUnstable() {
        return this.get(CraftTNT.UNSTABLE);
    }

    @Override
    public void setUnstable(boolean unstable) {
        this.set(CraftTNT.UNSTABLE, unstable);
    }
}
