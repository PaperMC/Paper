package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.ChiseledBookshelf;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftChiseledBookshelf extends CraftBlockData implements ChiseledBookshelf {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty[] SLOT_OCCUPIED = new net.minecraft.world.level.block.state.properties.BooleanProperty[]{
        getBoolean("slot_0_occupied"), getBoolean("slot_1_occupied"), getBoolean("slot_2_occupied"),
        getBoolean("slot_3_occupied"), getBoolean("slot_4_occupied"), getBoolean("slot_5_occupied")
    };

    @Override
    public boolean isSlotOccupied(int slot) {
        return this.get(CraftChiseledBookshelf.SLOT_OCCUPIED[slot]);
    }

    @Override
    public void setSlotOccupied(int slot, boolean has) {
        this.set(CraftChiseledBookshelf.SLOT_OCCUPIED[slot], has);
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
        return CraftChiseledBookshelf.SLOT_OCCUPIED.length;
    }
}
