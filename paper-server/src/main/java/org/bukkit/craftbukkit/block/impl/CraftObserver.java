/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftObserver extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Observer, org.bukkit.block.data.Directional, org.bukkit.block.data.Powerable {

    public CraftObserver() {
        super();
    }

    public CraftObserver(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.ObserverBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftObserver.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftObserver.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftObserver.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.ObserverBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftObserver.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftObserver.POWERED, powered);
    }
}
