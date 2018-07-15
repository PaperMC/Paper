/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftHugeMushroom extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.MultipleFacing {

    public CraftHugeMushroom() {
        super();
    }

    public CraftHugeMushroom(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftMultipleFacing

    private static final net.minecraft.server.BlockStateBoolean[] FACES = new net.minecraft.server.BlockStateBoolean[]{
        getBoolean(net.minecraft.server.BlockHugeMushroom.class, "north", true), getBoolean(net.minecraft.server.BlockHugeMushroom.class, "east", true), getBoolean(net.minecraft.server.BlockHugeMushroom.class, "south", true), getBoolean(net.minecraft.server.BlockHugeMushroom.class, "west", true), getBoolean(net.minecraft.server.BlockHugeMushroom.class, "up", true), getBoolean(net.minecraft.server.BlockHugeMushroom.class, "down", true)
    };

    @Override
    public boolean hasFace(org.bukkit.block.BlockFace face) {
        return get(FACES[face.ordinal()]);
    }

    @Override
    public void setFace(org.bukkit.block.BlockFace face, boolean has) {
        set(FACES[face.ordinal()], has);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        com.google.common.collect.ImmutableSet.Builder<org.bukkit.block.BlockFace> faces = com.google.common.collect.ImmutableSet.builder();

        for (int i = 0; i < FACES.length; i++) {
            if (FACES[i] != null && get(FACES[i])) {
                faces.add(org.bukkit.block.BlockFace.values()[i]);
            }
        }

        return faces.build();
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getAllowedFaces() {
        com.google.common.collect.ImmutableSet.Builder<org.bukkit.block.BlockFace> faces = com.google.common.collect.ImmutableSet.builder();

        for (int i = 0; i < FACES.length; i++) {
            if (FACES[i] != null) {
                faces.add(org.bukkit.block.BlockFace.values()[i]);
            }
        }

        return faces.build();
    }
}
