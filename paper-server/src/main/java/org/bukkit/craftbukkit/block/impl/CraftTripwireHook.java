/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTripwireHook extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.TripwireHook, org.bukkit.block.data.Attachable, org.bukkit.block.data.Directional, org.bukkit.block.data.Powerable {

    public CraftTripwireHook() {
        super();
    }

    public CraftTripwireHook(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftAttachable

    private static final net.minecraft.server.BlockStateBoolean ATTACHED = getBoolean(net.minecraft.server.BlockTripwireHook.class, "attached");

    @Override
    public boolean isAttached() {
        return get(ATTACHED);
    }

    @Override
    public void setAttached(boolean attached) {
        set(ATTACHED, attached);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.server.BlockStateEnum<?> FACING = getEnum(net.minecraft.server.BlockTripwireHook.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return get(FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        set(FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return getValues(FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.server.BlockStateBoolean POWERED = getBoolean(net.minecraft.server.BlockTripwireHook.class, "powered");

    @Override
    public boolean isPowered() {
        return get(POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        set(POWERED, powered);
    }
}
