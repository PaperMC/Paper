/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftMangroveLeaves extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Leaves {

    public CraftMangroveLeaves() {
        super();
    }

    public CraftMangroveLeaves(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftLeaves

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger DISTANCE = getInteger(net.minecraft.world.level.block.MangroveLeavesBlock.class, "distance");
    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean PERSISTENT = getBoolean(net.minecraft.world.level.block.MangroveLeavesBlock.class, "persistent");

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
