package org.bukkit.craftbukkit.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
public class ItemFlagsTest {

    @Test
    void testItemFlags() {
        final ItemStack item = ItemStack.of(Material.DIAMOND_HOE);

        for (final ItemFlag value : ItemFlag.values()) {
            assertFalse(item.hasItemFlag(value));
        }

        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        assertTrue(item.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES));
        assertTrue(item.getItemFlags().contains(ItemFlag.HIDE_ATTRIBUTES));

        item.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        assertFalse(item.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES));
        assertFalse(item.getItemFlags().contains(ItemFlag.HIDE_ATTRIBUTES));
    }
}
