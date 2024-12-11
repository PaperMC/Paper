/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftChiseledBookShelf extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.ChiseledBookshelf, org.bukkit.block.data.Directional {

    public CraftChiseledBookShelf() {
        super();
    }

    public CraftChiseledBookShelf(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftChiseledBookshelf

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty[] SLOT_OCCUPIED = new net.minecraft.world.level.block.state.properties.BooleanProperty[]{
        getBoolean(net.minecraft.world.level.block.ChiseledBookShelfBlock.class, "slot_0_occupied"), getBoolean(net.minecraft.world.level.block.ChiseledBookShelfBlock.class, "slot_1_occupied"), getBoolean(net.minecraft.world.level.block.ChiseledBookShelfBlock.class, "slot_2_occupied"),
        getBoolean(net.minecraft.world.level.block.ChiseledBookShelfBlock.class, "slot_3_occupied"), getBoolean(net.minecraft.world.level.block.ChiseledBookShelfBlock.class, "slot_4_occupied"), getBoolean(net.minecraft.world.level.block.ChiseledBookShelfBlock.class, "slot_5_occupied")
    };

    @Override
    public boolean isSlotOccupied(int slot) {
        return this.get(CraftChiseledBookShelf.SLOT_OCCUPIED[slot]);
    }

    @Override
    public void setSlotOccupied(int slot, boolean has) {
        this.set(CraftChiseledBookShelf.SLOT_OCCUPIED[slot], has);
    }

    @Override
    public java.util.Set<Integer> getOccupiedSlots() {
        com.google.common.collect.ImmutableSet.Builder<Integer> slots = com.google.common.collect.ImmutableSet.builder();

        for (int index = 0; index < this.getMaximumOccupiedSlots(); index++) {
            if (this.isSlotOccupied(index)) {
                slots.add(index);
            }
        }

        return slots.build();
    }

    @Override
    public int getMaximumOccupiedSlots() {
        return CraftChiseledBookShelf.SLOT_OCCUPIED.length;
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.ChiseledBookShelfBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftChiseledBookShelf.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftChiseledBookShelf.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftChiseledBookShelf.FACING, org.bukkit.block.BlockFace.class);
    }
}
