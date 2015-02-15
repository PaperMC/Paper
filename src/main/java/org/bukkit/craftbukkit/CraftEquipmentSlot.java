package org.bukkit.craftbukkit;

import org.bukkit.inventory.EquipmentSlot;

public class CraftEquipmentSlot {

    private static final int[] slots = new int[EquipmentSlot.values().length];
    private static final EquipmentSlot[] enums = new EquipmentSlot[EquipmentSlot.values().length];

    static {
        set(EquipmentSlot.HAND, 0);
        set(EquipmentSlot.FEET, 1);
        set(EquipmentSlot.LEGS, 2);
        set(EquipmentSlot.CHEST, 3);
        set(EquipmentSlot.HEAD, 4);
    }

    private static void set(EquipmentSlot type, int value) {
        slots[type.ordinal()] = value;
        if (value < enums.length) {
            enums[value] = type;
        }
    }

    public static EquipmentSlot getSlot(int magic) {
        if (magic > 0 && magic < enums.length) {
            return enums[magic];
        }
        return null;
    }

    public static int getSlotIndex(EquipmentSlot slot) {
        return slots[slot.ordinal()];
    }
}
