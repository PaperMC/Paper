/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCampfire extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Campfire, org.bukkit.block.data.Directional, org.bukkit.block.data.Lightable, org.bukkit.block.data.Waterlogged {

    public CraftCampfire() {
        super();
    }

    public CraftCampfire(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCampfire

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty SIGNAL_FIRE = getBoolean(net.minecraft.world.level.block.CampfireBlock.class, "signal_fire");

    @Override
    public boolean isSignalFire() {
        return this.get(CraftCampfire.SIGNAL_FIRE);
    }

    @Override
    public void setSignalFire(boolean signalFire) {
        this.set(CraftCampfire.SIGNAL_FIRE, signalFire);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.CampfireBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftCampfire.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftCampfire.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftCampfire.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftLightable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty LIT = getBoolean(net.minecraft.world.level.block.CampfireBlock.class, "lit");

    @Override
    public boolean isLit() {
        return this.get(CraftCampfire.LIT);
    }

    @Override
    public void setLit(boolean lit) {
        this.set(CraftCampfire.LIT, lit);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.CampfireBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftCampfire.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftCampfire.WATERLOGGED, waterlogged);
    }
}
