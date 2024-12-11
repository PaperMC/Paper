/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTripwire extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Tripwire, org.bukkit.block.data.Attachable, org.bukkit.block.data.MultipleFacing, org.bukkit.block.data.Powerable {

    public CraftTripwire() {
        super();
    }

    public CraftTripwire(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftTripwire

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty DISARMED = getBoolean(net.minecraft.world.level.block.TripWireBlock.class, "disarmed");

    @Override
    public boolean isDisarmed() {
        return this.get(CraftTripwire.DISARMED);
    }

    @Override
    public void setDisarmed(boolean disarmed) {
        this.set(CraftTripwire.DISARMED, disarmed);
    }

    // org.bukkit.craftbukkit.block.data.CraftAttachable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty ATTACHED = getBoolean(net.minecraft.world.level.block.TripWireBlock.class, "attached");

    @Override
    public boolean isAttached() {
        return this.get(CraftTripwire.ATTACHED);
    }

    @Override
    public void setAttached(boolean attached) {
        this.set(CraftTripwire.ATTACHED, attached);
    }

    // org.bukkit.craftbukkit.block.data.CraftMultipleFacing

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty[] FACES = new net.minecraft.world.level.block.state.properties.BooleanProperty[]{
        getBoolean(net.minecraft.world.level.block.TripWireBlock.class, "north", true), getBoolean(net.minecraft.world.level.block.TripWireBlock.class, "east", true), getBoolean(net.minecraft.world.level.block.TripWireBlock.class, "south", true), getBoolean(net.minecraft.world.level.block.TripWireBlock.class, "west", true), getBoolean(net.minecraft.world.level.block.TripWireBlock.class, "up", true), getBoolean(net.minecraft.world.level.block.TripWireBlock.class, "down", true)
    };

    @Override
    public boolean hasFace(org.bukkit.block.BlockFace face) {
        net.minecraft.world.level.block.state.properties.BooleanProperty state = CraftTripwire.FACES[face.ordinal()];
        if (state == null) {
            throw new IllegalArgumentException("Non-allowed face " + face + ". Check MultipleFacing.getAllowedFaces.");
        }
        return this.get(state);
    }

    @Override
    public void setFace(org.bukkit.block.BlockFace face, boolean has) {
        net.minecraft.world.level.block.state.properties.BooleanProperty state = CraftTripwire.FACES[face.ordinal()];
        if (state == null) {
            throw new IllegalArgumentException("Non-allowed face " + face + ". Check MultipleFacing.getAllowedFaces.");
        }
        this.set(state, has);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        com.google.common.collect.ImmutableSet.Builder<org.bukkit.block.BlockFace> faces = com.google.common.collect.ImmutableSet.builder();

        for (int i = 0; i < CraftTripwire.FACES.length; i++) {
            if (CraftTripwire.FACES[i] != null && this.get(CraftTripwire.FACES[i])) {
                faces.add(org.bukkit.block.BlockFace.values()[i]);
            }
        }

        return faces.build();
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getAllowedFaces() {
        com.google.common.collect.ImmutableSet.Builder<org.bukkit.block.BlockFace> faces = com.google.common.collect.ImmutableSet.builder();

        for (int i = 0; i < CraftTripwire.FACES.length; i++) {
            if (CraftTripwire.FACES[i] != null) {
                faces.add(org.bukkit.block.BlockFace.values()[i]);
            }
        }

        return faces.build();
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.TripWireBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftTripwire.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftTripwire.POWERED, powered);
    }
}
