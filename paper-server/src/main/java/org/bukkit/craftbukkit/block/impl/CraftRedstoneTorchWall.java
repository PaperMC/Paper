/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftRedstoneTorchWall extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.RedstoneWallTorch, org.bukkit.block.data.Directional, org.bukkit.block.data.Lightable {

    public CraftRedstoneTorchWall() {
        super();
    }

    public CraftRedstoneTorchWall(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.RedstoneWallTorchBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftRedstoneTorchWall.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftRedstoneTorchWall.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftRedstoneTorchWall.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftLightable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty LIT = getBoolean(net.minecraft.world.level.block.RedstoneWallTorchBlock.class, "lit");

    @Override
    public boolean isLit() {
        return this.get(CraftRedstoneTorchWall.LIT);
    }

    @Override
    public void setLit(boolean lit) {
        this.set(CraftRedstoneTorchWall.LIT, lit);
    }
}
