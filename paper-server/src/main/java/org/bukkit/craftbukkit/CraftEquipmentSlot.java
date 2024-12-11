package org.bukkit.craftbukkit;

import java.util.Locale;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlotGroup;
import org.bukkit.inventory.EquipmentSlot;

public class CraftEquipmentSlot {

    private static final net.minecraft.world.entity.EquipmentSlot[] slots = new net.minecraft.world.entity.EquipmentSlot[EquipmentSlot.values().length];
    private static final EquipmentSlot[] enums = new EquipmentSlot[net.minecraft.world.entity.EquipmentSlot.values().length];

    static {
        set(EquipmentSlot.HAND, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        set(EquipmentSlot.OFF_HAND, net.minecraft.world.entity.EquipmentSlot.OFFHAND);
        set(EquipmentSlot.FEET, net.minecraft.world.entity.EquipmentSlot.FEET);
        set(EquipmentSlot.LEGS, net.minecraft.world.entity.EquipmentSlot.LEGS);
        set(EquipmentSlot.CHEST, net.minecraft.world.entity.EquipmentSlot.CHEST);
        set(EquipmentSlot.HEAD, net.minecraft.world.entity.EquipmentSlot.HEAD);
        set(EquipmentSlot.BODY, net.minecraft.world.entity.EquipmentSlot.BODY);
    }

    private static void set(EquipmentSlot type, net.minecraft.world.entity.EquipmentSlot value) {
        CraftEquipmentSlot.slots[type.ordinal()] = value;
        CraftEquipmentSlot.enums[value.ordinal()] = type;
    }

    public static EquipmentSlot getSlot(net.minecraft.world.entity.EquipmentSlot nms) {
        return CraftEquipmentSlot.enums[nms.ordinal()];
    }

    public static org.bukkit.inventory.EquipmentSlotGroup getSlot(EquipmentSlotGroup nms) {
        return org.bukkit.inventory.EquipmentSlotGroup.getByName(nms.getSerializedName());
    }

    public static net.minecraft.world.entity.EquipmentSlot getNMS(EquipmentSlot slot) {
        return CraftEquipmentSlot.slots[slot.ordinal()];
    }

    public static EquipmentSlotGroup getNMSGroup(org.bukkit.inventory.EquipmentSlotGroup slot) {
        return EquipmentSlotGroup.valueOf(slot.toString().toUpperCase(Locale.ROOT));
    }

    public static EquipmentSlot getHand(InteractionHand enumhand) {
        return (enumhand == InteractionHand.MAIN_HAND) ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
    }

    public static InteractionHand getHand(EquipmentSlot hand) {
        if (hand == EquipmentSlot.HAND) {
            return InteractionHand.MAIN_HAND;
        } else if (hand == EquipmentSlot.OFF_HAND) {
            return InteractionHand.OFF_HAND;
        }

        throw new IllegalArgumentException("EquipmentSlot." + hand + " is not a hand");
    }
}
