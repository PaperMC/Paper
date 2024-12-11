/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftWitherSkullWall extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.WallSkull, org.bukkit.block.data.Directional, org.bukkit.block.data.Powerable {

    public CraftWitherSkullWall() {
        super();
    }

    public CraftWitherSkullWall(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.WitherWallSkullBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftWitherSkullWall.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftWitherSkullWall.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftWitherSkullWall.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.WitherWallSkullBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftWitherSkullWall.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftWitherSkullWall.POWERED, powered);
    }
}
