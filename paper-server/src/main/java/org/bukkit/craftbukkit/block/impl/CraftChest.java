/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftChest extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Chest, org.bukkit.block.data.Directional, org.bukkit.block.data.Waterlogged {

    public CraftChest() {
        super();
    }

    public CraftChest(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftChest

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> TYPE = getEnum(net.minecraft.world.level.block.ChestBlock.class, "type");

    @Override
    public org.bukkit.block.data.type.Chest.Type getType() {
        return this.get(CraftChest.TYPE, org.bukkit.block.data.type.Chest.Type.class);
    }

    @Override
    public void setType(org.bukkit.block.data.type.Chest.Type type) {
        this.set(CraftChest.TYPE, type);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.ChestBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftChest.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftChest.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftChest.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.ChestBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftChest.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftChest.WATERLOGGED, waterlogged);
    }
}
