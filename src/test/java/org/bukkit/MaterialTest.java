package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Collections;
import java.util.Map;

import net.minecraft.server.IRegistry;
import net.minecraft.server.Item;
import net.minecraft.server.MinecraftKey;

import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

import com.google.common.collect.Maps;
import java.util.Iterator;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

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
            String name = item.getName();

            Material material = materials.remove(id);

            assertThat("Missing " + name + "(" + id + ")", material, is(not(nullValue())));
            assertNotNull("No item mapping for " + name, CraftMagicNumbers.getMaterial(item));
        }

        assertThat(materials, is(Collections.EMPTY_MAP));
    }
}
