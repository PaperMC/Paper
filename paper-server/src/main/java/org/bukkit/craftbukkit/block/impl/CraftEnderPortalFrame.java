/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftEnderPortalFrame extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.EndPortalFrame, org.bukkit.block.data.Directional {

    public CraftEnderPortalFrame() {
        super();
    }

    public CraftEnderPortalFrame(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftEndPortalFrame

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty EYE = getBoolean(net.minecraft.world.level.block.EndPortalFrameBlock.class, "eye");

    @Override
    public boolean hasEye() {
        return this.get(CraftEnderPortalFrame.EYE);
    }

    @Override
    public void setEye(boolean eye) {
        this.set(CraftEnderPortalFrame.EYE, eye);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.EndPortalFrameBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftEnderPortalFrame.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftEnderPortalFrame.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftEnderPortalFrame.FACING, org.bukkit.block.BlockFace.class);
    }
}
