/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftGrindstone extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Grindstone, org.bukkit.block.data.Directional, org.bukkit.block.data.FaceAttachable {

    public CraftGrindstone() {
        super();
    }

    public CraftGrindstone(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.GrindstoneBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftGrindstone.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftGrindstone.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftGrindstone.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftFaceAttachable

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> ATTACH_FACE = getEnum(net.minecraft.world.level.block.GrindstoneBlock.class, "face");

    @Override
    public org.bukkit.block.data.FaceAttachable.AttachedFace getAttachedFace() {
        return this.get(CraftGrindstone.ATTACH_FACE, org.bukkit.block.data.FaceAttachable.AttachedFace.class);
    }

    @Override
    public void setAttachedFace(org.bukkit.block.data.FaceAttachable.AttachedFace face) {
        this.set(CraftGrindstone.ATTACH_FACE, face);
    }
}
