package org.bukkit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Item;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class MaterialTest extends AbstractTestingBase {

    @Test
    public void verifyMapping() {
        Map<MinecraftKey, Material> materials = Maps.newHashMap();
        for (Material material : Material.values()) {
            if (INVALIDATED_MATERIALS.contains(material)) {
                continue;
            }

            materials.put(CraftMagicNumbers.key(material), material);
        }

        Iterator<Item> items = IRegistry.ITEM.iterator();

        while (items.hasNext()) {
            Item item = items.next();
            if (item == null) continue;

            MinecraftKey id = IRegistry.ITEM.getKey(item);
            String name = item.getDescriptionId();

            Material material = materials.remove(id);

            assertThat("Missing " + name + "(" + id + ")", material, is(not(nullValue())));
            assertNotNull("No item mapping for " + name, CraftMagicNumbers.getMaterial(item));
        }

        assertThat(materials, is(Collections.EMPTY_MAP));
    }

    @Test
    public void verifyMaterialOrder() {
        List<Material> expectedOrder = new ArrayList<>(Material.values().length);

        // Start with items in the same order as IRegistry.ITEM
        StreamSupport.stream(IRegistry.ITEM.spliterator(), false)
                .map(CraftMagicNumbers::getMaterial)
                .forEach(expectedOrder::add);

        // Then non-item blocks in the same order as IRegistry.BLOCK
        StreamSupport.stream(IRegistry.BLOCK.spliterator(), false)
                .map(CraftMagicNumbers::getMaterial)
                .filter(block -> !block.isItem())
                .forEach(expectedOrder::add);

        // Then legacy materials in order of ID
        Arrays.stream(Material.values())
                .filter(Material::isLegacy)
                .sorted(Comparator.comparingInt(Material::getId))
                .forEach(expectedOrder::add);

        assertArrayEquals(expectedOrder.toArray(), Material.values());
    }
}
