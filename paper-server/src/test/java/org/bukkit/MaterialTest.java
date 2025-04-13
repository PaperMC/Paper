package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.LegacyHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class MaterialTest {

    @Test
    public void verifyMapping() {
        Map<ResourceLocation, Material> materials = Maps.newHashMap();
        for (Material material : Material.values()) {
            if (LegacyHelper.getInvalidatedMaterials().contains(material)) {
                continue;
            }

            materials.put(CraftNamespacedKey.toMinecraft(material.getKey()), material);
        }

        Iterator<Item> items = BuiltInRegistries.ITEM.iterator();

        while (items.hasNext()) {
            Item item = items.next();
            if (item == null) continue;

            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            String name = item.getDescriptionId();

            Material material = materials.remove(id);

            assertThat(material, is(not(nullValue())), "Missing " + name + "(" + id + ")");
            assertNotNull(CraftMagicNumbers.getMaterial(item), "No item mapping for " + name);
        }

        assertThat(materials, is(Collections.EMPTY_MAP));
    }
}
