/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftMultiface extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.ResinClump, org.bukkit.block.data.MultipleFacing, org.bukkit.block.data.Waterlogged {

    public CraftMultiface() {
        super();
    }

    public CraftMultiface(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftMultipleFacing

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty[] FACES = new net.minecraft.world.level.block.state.properties.BooleanProperty[]{
        getBoolean(net.minecraft.world.level.block.MultifaceBlock.class, "north", true), getBoolean(net.minecraft.world.level.block.MultifaceBlock.class, "east", true), getBoolean(net.minecraft.world.level.block.MultifaceBlock.class, "south", true), getBoolean(net.minecraft.world.level.block.MultifaceBlock.class, "west", true), getBoolean(net.minecraft.world.level.block.MultifaceBlock.class, "up", true), getBoolean(net.minecraft.world.level.block.MultifaceBlock.class, "down", true)
    };

    @Override
    public boolean hasFace(org.bukkit.block.BlockFace face) {
        net.minecraft.world.level.block.state.properties.BooleanProperty state = CraftMultiface.FACES[face.ordinal()];
        if (state == null) {
            throw new IllegalArgumentException("Non-allowed face " + face + ". Check MultipleFacing.getAllowedFaces.");
        }
        return this.get(state);
    }

    @Override
    public void setFace(org.bukkit.block.BlockFace face, boolean has) {
        net.minecraft.world.level.block.state.properties.BooleanProperty state = CraftMultiface.FACES[face.ordinal()];
        if (state == null) {
            throw new IllegalArgumentException("Non-allowed face " + face + ". Check MultipleFacing.getAllowedFaces.");
        }
        this.set(state, has);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        com.google.common.collect.ImmutableSet.Builder<org.bukkit.block.BlockFace> faces = com.google.common.collect.ImmutableSet.builder();

        for (int i = 0; i < CraftMultiface.FACES.length; i++) {
            if (CraftMultiface.FACES[i] != null && this.get(CraftMultiface.FACES[i])) {
                faces.add(org.bukkit.block.BlockFace.values()[i]);
            }
        }

        return faces.build();
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getAllowedFaces() {
        com.google.common.collect.ImmutableSet.Builder<org.bukkit.block.BlockFace> faces = com.google.common.collect.ImmutableSet.builder();

        for (int i = 0; i < CraftMultiface.FACES.length; i++) {
            if (CraftMultiface.FACES[i] != null) {
                faces.add(org.bukkit.block.BlockFace.values()[i]);
            }
        }

        return faces.build();
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.MultifaceBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftMultiface.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftMultiface.WATERLOGGED, waterlogged);
    }
}
