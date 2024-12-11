package io.papermc.paper.inventory.item;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.Equippable;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
public class ArmorSlotTypeMaterialTest {

    public static Stream<Object[]> slotTypeParams() {
        final List<Object[]> parameters = new ArrayList<>();
        for (final PlayerArmorChangeEvent.SlotType slotType : PlayerArmorChangeEvent.SlotType.values()) {
            for (final Material item : slotType.getTypes()) {
                parameters.add(new Object[]{ slotType, item });
            }
        }
        return parameters.stream();
    }

    @ParameterizedTest(name = "{argumentsWithNames}")
    @MethodSource("slotTypeParams")
    public void testSlotType(PlayerArmorChangeEvent.SlotType slotType, Material item) {
        final Item nmsItem = CraftMagicNumbers.getItem(item);
        final Equippable equippable = nmsItem.components().get(DataComponents.EQUIPPABLE);
        assertNotNull(equippable, item + " isn't equipable");
        final EquipmentSlot slot = switch (slotType) {
            case HEAD -> EquipmentSlot.HEAD;
            case CHEST -> EquipmentSlot.CHEST;
            case LEGS -> EquipmentSlot.LEGS;
            case FEET -> EquipmentSlot.FEET;
        };
        assertEquals(equippable.slot(), slot, item + " isn't set to the right slot");
    }

    public static Stream<Object[]> equipableParams() {
        final List<Object[]> parameters = new ArrayList<>();
        for (final Item item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
            final Equippable equippable = item.components().get(DataComponents.EQUIPPABLE);
            if (equippable != null) {
                parameters.add(new Object[]{equippable, item});
            }
        }
        return parameters.stream();
    }

    @ParameterizedTest(name = "{argumentsWithNames}")
    @MethodSource("equipableParams")
    public void testEquipable(Equippable equipable, Item item) {
        final EquipmentSlot equipmentSlot = equipable.slot();
        PlayerArmorChangeEvent.SlotType slotType = switch (equipmentSlot) {
            case HEAD -> PlayerArmorChangeEvent.SlotType.HEAD;
            case CHEST -> PlayerArmorChangeEvent.SlotType.CHEST;
            case LEGS -> PlayerArmorChangeEvent.SlotType.LEGS;
            case FEET -> PlayerArmorChangeEvent.SlotType.FEET;
            default -> null;
        };
        if (slotType != null) {
            assertTrue(slotType.getTypes().contains(CraftMagicNumbers.getMaterial(item)), "SlotType " + slotType + " doesn't include " + item);
        }
    }
}
