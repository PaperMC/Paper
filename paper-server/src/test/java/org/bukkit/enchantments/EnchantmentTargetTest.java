package org.bukkit.enchantments;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentSlotType;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class EnchantmentTargetTest extends AbstractTestingBase {

    @Test
    public void test() {
        for (EnchantmentSlotType nmsSlot : EnchantmentSlotType.values()) {
            EnchantmentTarget bukkitTarget;
            switch (nmsSlot) {
                case ARMOR_CHEST:
                    bukkitTarget = EnchantmentTarget.ARMOR_TORSO;
                    break;
                case DIGGER:
                    bukkitTarget = EnchantmentTarget.TOOL;
                    break;
                default:
                    bukkitTarget = EnchantmentTarget.valueOf(nmsSlot.name());
                    break;
            }

            assertNotNull(bukkitTarget, "No bukkit target for slot " + nmsSlot);

            for (Item item : BuiltInRegistries.ITEM) {
                Material material = CraftItemType.minecraftToBukkit(item);

                boolean nms = nmsSlot.canEnchant(item);
                boolean bukkit = bukkitTarget.includes(material);

                assertEquals(nms, bukkit, "Slot mismatch for " + bukkitTarget + " and " + material);
            }
        }
    }
}
