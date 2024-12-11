/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftBeehive extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Beehive, org.bukkit.block.data.Directional {

    public CraftBeehive() {
        super();
    }

    public CraftBeehive(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftBeehive

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty HONEY_LEVEL = getInteger(net.minecraft.world.level.block.BeehiveBlock.class, "honey_level");

    @Override
    public int getHoneyLevel() {
        return this.get(CraftBeehive.HONEY_LEVEL);
    }

    @Override
    public void setHoneyLevel(int honeyLevel) {
        this.set(CraftBeehive.HONEY_LEVEL, honeyLevel);
    }

    @Override
    public int getMaximumHoneyLevel() {
        return getMax(CraftBeehive.HONEY_LEVEL);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.BeehiveBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftBeehive.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftBeehive.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftBeehive.FACING, org.bukkit.block.BlockFace.class);
    }
}
