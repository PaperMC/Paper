package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Locale;
import org.bukkit.material.MaterialData;
import org.junit.jupiter.api.Test;

public class MaterialTest {
    @Test
    public void getByName() {
        for (Material material : Material.values()) {
            assertThat(Material.getMaterial(material.toString()), is(material));
        }
    }

    @Test
    public void getByNameNull() {
        assertThat(Material.getMaterial(null), is(nullValue()));
    }

    @Test
    public void getData() {
        for (Material material : Material.values()) {
            if (!material.isLegacy()) {
                continue;
            }
            Class<? extends MaterialData> clazz = material.getData();

            assertThat(material.getNewData((byte) 0), is(instanceOf(clazz)));
        }
    }

    @Test
    public void matchMaterialByNull() {
        assertThrows(IllegalArgumentException.class, () -> Material.matchMaterial(null));
    }

    @Test
    public void matchMaterialByName() {
        for (Material material : Material.values()) {
            assertThat(Material.matchMaterial(material.toString()), is(material));
        }
    }

    @Test
    public void matchMaterialByKey() {
        for (Material material : Material.values()) {
            if (material.isLegacy()) {
                continue;
            }
            assertThat(Material.matchMaterial(material.getKey().toString()), is(material));
        }
    }

    @Test
    public void matchMaterialByWrongNamespace() {
        for (Material material : Material.values()) {
            if (material.isLegacy()) {
                continue;
            }
            assertNull(Material.matchMaterial("bogus:" + material.getKey().getKey()));
        }
    }

    @Test
    public void matchMaterialByLowerCaseAndSpaces() {
        for (Material material : Material.values()) {
            String name = material.toString().replaceAll("_", " ").toLowerCase(Locale.ROOT);
            assertThat(Material.matchMaterial(name), is(material));
        }
    }
}
