package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Map;

import net.minecraft.server.Item;
import net.minecraft.server.ItemFood;

import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

import com.google.common.collect.Maps;

public class MaterialTest extends AbstractTestingBase {
    @Test
    public void verifyMapping() {
        Map<Integer, Material> materials = Maps.newHashMap();
        for (Material material : Material.values()) {
            materials.put(material.getId(), material);
        }
        materials.remove(0); // Purge air.

        for (Item item : Item.byId) {
            if (item == null) continue;

            int id = item.id;
            String name = item.getName();
            int maxStackSize = item.getMaxStackSize();
            int maxDurability = item.getMaxDurability();

            Material material = materials.remove(id);

            assertNotNull(String.format("org.bukkit.Material is missing id: %d named: %s", id, name), material);

            assertThat(String.format("org.bukkit.Material.%s maxStackSize:", material), material.getMaxStackSize(), is(maxStackSize));
            assertThat(String.format("org.bukkit.Material.%s maxDurability:", material), material.getMaxDurability(), is((short) maxDurability));
        }

        assertThat("org.bukkit.Material has too many entries", materials.values(), hasSize(0));
    }

    @Test
    public void verifyIsEdible() {
        Map<Integer, Material> materials = Maps.newHashMap();
        for (Material material : Material.values()) {
            if (!material.isEdible()) continue;
            materials.put(material.getId(), material);
        }

        for (Item item : Item.byId) {
            if (item == null) continue;
            if (!(item instanceof ItemFood)) continue;

            int id = item.id;
            String name = item.getName();

            Material material = materials.remove(id);

            assertNotNull(String.format("org.bukkit.Material does not list id: %d named: %s edible", id, name), material);
        }

        assertThat("org.bukkit.Material has entries marked edible that are not ItemFood", materials.values(), hasSize(0));
    }
}
