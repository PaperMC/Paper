package org.bukkit.craftbukkit;

import java.util.Locale;
import com.google.common.collect.BiMap;
import com.google.common.collect.EnumBiMap;
import net.minecraft.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlotGroup;
import org.bukkit.inventory.EquipmentSlot;

public final class CraftEquipmentSlot {

    private CraftEquipmentSlot() {
    }

    private static final BiMap<net.minecraft.world.entity.EquipmentSlot, EquipmentSlot> BRIDGE =
        Util.make(EnumBiMap.create(net.minecraft.world.entity.EquipmentSlot.class, EquipmentSlot.class), data -> {
            data.put(net.minecraft.world.entity.EquipmentSlot.MAINHAND, EquipmentSlot.HAND);
            data.put(net.minecraft.world.entity.EquipmentSlot.OFFHAND, EquipmentSlot.OFF_HAND);
            data.put(net.minecraft.world.entity.EquipmentSlot.FEET, EquipmentSlot.FEET);
            data.put(net.minecraft.world.entity.EquipmentSlot.LEGS, EquipmentSlot.LEGS);
            data.put(net.minecraft.world.entity.EquipmentSlot.CHEST, EquipmentSlot.CHEST);
            data.put(net.minecraft.world.entity.EquipmentSlot.HEAD, EquipmentSlot.HEAD);
            data.put(net.minecraft.world.entity.EquipmentSlot.BODY, EquipmentSlot.BODY);
            data.put(net.minecraft.world.entity.EquipmentSlot.SADDLE, EquipmentSlot.SADDLE);
        });

    public static EquipmentSlot getSlot(net.minecraft.world.entity.EquipmentSlot slot) {
        return BRIDGE.get(slot);
    }

    public static org.bukkit.inventory.EquipmentSlotGroup getSlotGroup(EquipmentSlotGroup slotGroup) {
        return org.bukkit.inventory.EquipmentSlotGroup.getByName(slotGroup.getSerializedName());
    }

    public static net.minecraft.world.entity.EquipmentSlot getNMS(EquipmentSlot slot) {
        return BRIDGE.inverse().get(slot);
    }

    public static EquipmentSlotGroup getNMSGroup(org.bukkit.inventory.EquipmentSlotGroup slot) {
        return EquipmentSlotGroup.valueOf(slot.toString().toUpperCase(Locale.ROOT));
    }

    public static EquipmentSlot getHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
    }

    public static InteractionHand getHand(EquipmentSlot hand) {
        if (hand == EquipmentSlot.HAND) {
            return InteractionHand.MAIN_HAND;
        }
        if (hand == EquipmentSlot.OFF_HAND) {
            return InteractionHand.OFF_HAND;
        }

        throw new IllegalArgumentException("EquipmentSlot." + hand + " is not a hand");
    }
}
