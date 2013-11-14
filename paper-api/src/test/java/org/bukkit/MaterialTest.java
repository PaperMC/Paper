package org.bukkit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.bukkit.material.MaterialData;
import org.junit.Test;

public class MaterialTest {
    @Test
    public void getByName() {
        for (Material material : Material.values()) {
            assertThat(Material.getMaterial(material.toString()), is(material));
        }
    }

    @Test
    public void getById() throws Throwable {
        for (Material material : Material.values()) {
            if (material.getClass().getField(material.name()).getAnnotation(Deprecated.class) != null) {
                continue;
            }
            assertThat(Material.getMaterial(material.getId()), is(material));
        }
    }

    @Test
    public void isBlock() {
        for (Material material : Material.values()) {
            if (material.getId() > 255) continue;

            assertTrue(String.format("[%d] %s", material.getId(), material.toString()), material.isBlock());
        }
    }

    @Test
    public void getByOutOfRangeId() {
        assertThat(Material.getMaterial(Integer.MAX_VALUE), is(nullValue()));
        assertThat(Material.getMaterial(Integer.MIN_VALUE), is(nullValue()));
    }

    @Test
    public void getByNameNull() {
        assertThat(Material.getMaterial(null), is(nullValue()));
    }

    @Test
    public void getData() {
        for (Material material : Material.values()) {
            Class<? extends MaterialData> clazz = material.getData();

            assertThat(material.getNewData((byte) 0), is(instanceOf(clazz)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void matchMaterialByNull() {
        Material.matchMaterial(null);
    }

    @Test
    public void matchMaterialById() throws Throwable {
        for (Material material : Material.values()) {
            if (material.getClass().getField(material.name()).getAnnotation(Deprecated.class) != null) {
                continue;
            }
            assertThat(Material.matchMaterial(String.valueOf(material.getId())), is(material));
        }
    }

    @Test
    public void matchMaterialByName() {
        for (Material material : Material.values()) {
            assertThat(Material.matchMaterial(material.toString()), is(material));
        }
    }

    @Test
    public void matchMaterialByLowerCaseAndSpaces() {
        for (Material material : Material.values()) {
            String name = material.toString().replaceAll("_", " ").toLowerCase();
            assertThat(Material.matchMaterial(name), is(material));
        }
    }
}
