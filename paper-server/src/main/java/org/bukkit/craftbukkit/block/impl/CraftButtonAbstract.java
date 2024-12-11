/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftButtonAbstract extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Switch, org.bukkit.block.data.Directional, org.bukkit.block.data.FaceAttachable, org.bukkit.block.data.Powerable {

    public CraftButtonAbstract() {
        super();
    }

    public CraftButtonAbstract(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSwitch

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACE = getEnum(net.minecraft.world.level.block.ButtonBlock.class, "face");

    @Override
    public org.bukkit.block.data.type.Switch.Face getFace() {
        return this.get(CraftButtonAbstract.FACE, org.bukkit.block.data.type.Switch.Face.class);
    }

    @Override
    public void setFace(org.bukkit.block.data.type.Switch.Face face) {
        this.set(CraftButtonAbstract.FACE, face);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.ButtonBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftButtonAbstract.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftButtonAbstract.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftButtonAbstract.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftFaceAttachable

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> ATTACH_FACE = getEnum(net.minecraft.world.level.block.ButtonBlock.class, "face");

    @Override
    public org.bukkit.block.data.FaceAttachable.AttachedFace getAttachedFace() {
        return this.get(CraftButtonAbstract.ATTACH_FACE, org.bukkit.block.data.FaceAttachable.AttachedFace.class);
    }

    @Override
    public void setAttachedFace(org.bukkit.block.data.FaceAttachable.AttachedFace face) {
        this.set(CraftButtonAbstract.ATTACH_FACE, face);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.ButtonBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftButtonAbstract.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftButtonAbstract.POWERED, powered);
    }
}
