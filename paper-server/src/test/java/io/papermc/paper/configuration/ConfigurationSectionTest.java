package io.papermc.paper.configuration;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class ConfigurationSectionTest {
    public abstract ConfigurationSection getConfigurationSection();

    @Test
    public void testGetItemStack_String() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        ItemStack value = new ItemStack(Material.ACACIA_WOOD, 50);

        section.set(key, value);

        assertEquals(value, section.getItemStack(key));
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetItemStack_String_ItemStack() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        ItemStack value = new ItemStack(Material.ACACIA_WOOD, 50);
        ItemStack def = new ItemStack(Material.STONE, 1);

        section.set(key, value);

        assertEquals(value, section.getItemStack(key, def));
        assertEquals(def, section.getItemStack("doesntExist", def));
    }

    @Test
    public void testIsItemStack() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        ItemStack value = new ItemStack(Material.ACACIA_WOOD, 50);

        section.set(key, value);

        assertTrue(section.isItemStack(key));
        assertFalse(section.isItemStack("doesntExist"));
    }
}
