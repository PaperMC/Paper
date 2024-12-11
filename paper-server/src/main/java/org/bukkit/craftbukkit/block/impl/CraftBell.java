/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftBell extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Bell, org.bukkit.block.data.Directional, org.bukkit.block.data.Powerable {

    public CraftBell() {
        super();
    }

    public CraftBell(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftBell

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> ATTACHMENT = getEnum(net.minecraft.world.level.block.BellBlock.class, "attachment");

    @Override
    public org.bukkit.block.data.type.Bell.Attachment getAttachment() {
        return this.get(CraftBell.ATTACHMENT, org.bukkit.block.data.type.Bell.Attachment.class);
    }

    @Override
    public void setAttachment(org.bukkit.block.data.type.Bell.Attachment leaves) {
        this.set(CraftBell.ATTACHMENT, leaves);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.BellBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftBell.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftBell.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftBell.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.BellBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftBell.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftBell.POWERED, powered);
    }
}
