/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftChorusFruit extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.MultipleFacing {

    public CraftChorusFruit() {
        super();
    }

    public CraftChorusFruit(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftMultipleFacing

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty[] FACES = new net.minecraft.world.level.block.state.properties.BooleanProperty[]{
        getBoolean(net.minecraft.world.level.block.ChorusPlantBlock.class, "north", true), getBoolean(net.minecraft.world.level.block.ChorusPlantBlock.class, "east", true), getBoolean(net.minecraft.world.level.block.ChorusPlantBlock.class, "south", true), getBoolean(net.minecraft.world.level.block.ChorusPlantBlock.class, "west", true), getBoolean(net.minecraft.world.level.block.ChorusPlantBlock.class, "up", true), getBoolean(net.minecraft.world.level.block.ChorusPlantBlock.class, "down", true)
    };

    @Override
    public boolean hasFace(org.bukkit.block.BlockFace face) {
        net.minecraft.world.level.block.state.properties.BooleanProperty state = CraftChorusFruit.FACES[face.ordinal()];
        if (state == null) {
            throw new IllegalArgumentException("Non-allowed face " + face + ". Check MultipleFacing.getAllowedFaces.");
        }
        return this.get(state);
    }

    @Override
    public void setFace(org.bukkit.block.BlockFace face, boolean has) {
        net.minecraft.world.level.block.state.properties.BooleanProperty state = CraftChorusFruit.FACES[face.ordinal()];
        if (state == null) {
            throw new IllegalArgumentException("Non-allowed face " + face + ". Check MultipleFacing.getAllowedFaces.");
        }
        this.set(state, has);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        com.google.common.collect.ImmutableSet.Builder<org.bukkit.block.BlockFace> faces = com.google.common.collect.ImmutableSet.builder();

        for (int i = 0; i < CraftChorusFruit.FACES.length; i++) {
            if (CraftChorusFruit.FACES[i] != null && this.get(CraftChorusFruit.FACES[i])) {
                faces.add(org.bukkit.block.BlockFace.values()[i]);
            }
        }

        return faces.build();
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getAllowedFaces() {
        com.google.common.collect.ImmutableSet.Builder<org.bukkit.block.BlockFace> faces = com.google.common.collect.ImmutableSet.builder();

        for (int i = 0; i < CraftChorusFruit.FACES.length; i++) {
            if (CraftChorusFruit.FACES[i] != null) {
                faces.add(org.bukkit.block.BlockFace.values()[i]);
            }
        }

        return faces.build();
    }
}
