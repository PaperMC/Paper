package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Collections;
import java.util.Map;

import net.minecraft.server.Item;

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

            Material material = materials.remove(id);

            assertThat("Missing " + name + "(" + id + ")", material, is(not(nullValue())));
        }

        assertThat(materials, is(Collections.EMPTY_MAP));
    }
}
