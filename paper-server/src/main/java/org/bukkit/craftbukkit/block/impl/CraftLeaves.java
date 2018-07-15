/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftLeaves extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Leaves {

    public CraftLeaves() {
        super();
    }

    public CraftLeaves(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftLeaves

    private static final net.minecraft.server.BlockStateInteger DISTANCE = getInteger(net.minecraft.server.BlockLeaves.class, "distance");
    private static final net.minecraft.server.BlockStateBoolean PERSISTENT = getBoolean(net.minecraft.server.BlockLeaves.class, "persistent");

    @Override
    public boolean isPersistent() {
        return get(PERSISTENT);
    }

    @Override
    public void setPersistent(boolean persistent) {
        set(PERSISTENT, persistent);
    }

    @Override
    public int getDistance() {
        return get(DISTANCE);
    }

    @Override
    public void setDistance(int distance) {
        set(DISTANCE, distance);
    }
}
