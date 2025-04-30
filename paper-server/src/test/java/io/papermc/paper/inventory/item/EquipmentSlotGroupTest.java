package io.papermc.paper.inventory.item;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Normal
class EquipmentSlotGroupTest {

    static List<EquipmentSlotGroup> apiValues() throws ReflectiveOperationException {
        final List<EquipmentSlotGroup> apiValues = new ArrayList<>();
        for (final Field field : EquipmentSlotGroup.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || !Modifier.isFinal(field.getModifiers()) || !field.getType().equals(EquipmentSlotGroup.class)) {
                continue;
            }
            apiValues.add((EquipmentSlotGroup) field.get(null));
        }
        if (apiValues.isEmpty()) {
            throw new RuntimeException("Didn't find any api " + EquipmentSlotGroup.class.getSimpleName());
        }
        return apiValues;
    }

    @ParameterizedTest
    @MethodSource("apiValues")
    void testBukkitToNms(final EquipmentSlotGroup slotGroup) {
        final net.minecraft.world.entity.EquipmentSlotGroup nmsGroup = CraftEquipmentSlot.getNMSGroup(slotGroup);
        assertNotNull(nmsGroup, "No nms slot group found for " + slotGroup);
        assertEquals(nmsGroup.getSerializedName(), slotGroup.toString(), "slot group name mismatch");
        for (final EquipmentSlot slot : EquipmentSlot.values()) {
            assertEquals(nmsGroup.test(slot), slotGroup.test(CraftEquipmentSlot.getSlot(slot)));
        }
    }

    @ParameterizedTest
    @EnumSource(net.minecraft.world.entity.EquipmentSlotGroup.class)
    void testNmsToBukkit(final net.minecraft.world.entity.EquipmentSlotGroup slotGroup) {
        final EquipmentSlotGroup apiGroup = CraftEquipmentSlot.getSlotGroup(slotGroup);
        assertNotNull(apiGroup, "No api slot group found for " + slotGroup);
        assertEquals(apiGroup.toString(), slotGroup.getSerializedName(), "slot group name mismatch");
    }
}
