package org.bukkit.craftbukkit.inventory;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FactoryItemMaterialTest extends AbstractTestingBase {
    static final ItemFactory factory = CraftItemFactory.instance();
    static final StringBuilder buffer = new StringBuilder();
    static final Material[] materials = Material.values();

    static String name(Enum<?> from, Enum<?> to) {
        if (from.getClass() == to.getClass()) {
            return buffer.delete(0, Integer.MAX_VALUE).append(from.getClass().getName()).append(' ').append(from.name()).append(" to ").append(to.name()).toString();
        }
        return buffer.delete(0, Integer.MAX_VALUE).append(from.getClass().getName()).append('(').append(from.name()).append(") to ").append(to.getClass().getName()).append('(').append(to.name()).append(')').toString();
    }

    @Parameters(name="Material[{index}]:{0}")
    public static List<Object[]> data() {
        List<Object[]> list = new ArrayList<Object[]>();
        for (Material material : materials) {
            list.add(new Object[] {material});
        }
        return list;
    }

    @Parameter(0) public Material material;

    @Test
    public void itemStack() {
        ItemStack bukkitStack = new ItemStack(material);
        CraftItemStack craftStack = CraftItemStack.asCraftCopy(bukkitStack);
        ItemMeta meta = factory.getItemMeta(material);
        if (meta == null) {
            assertThat(material, is(Material.AIR));
        } else {
            assertTrue(factory.isApplicable(meta, bukkitStack));
            assertTrue(factory.isApplicable(meta, craftStack));
        }
    }

    @Test
    public void generalCase() {
        CraftMetaItem meta = (CraftMetaItem) factory.getItemMeta(material);
        if (meta == null) {
            assertThat(material, is(Material.AIR));
        } else {
            assertTrue(factory.isApplicable(meta, material));
            assertTrue(meta.applicableTo(material));

            meta = meta.clone();
            assertTrue(factory.isApplicable(meta, material));
            assertTrue(meta.applicableTo(material));
        }
    }

    @Test
    public void asMetaFor() {
        final CraftMetaItem baseMeta = (CraftMetaItem) factory.getItemMeta(material);
        if (baseMeta == null) {
            assertThat(material, is(Material.AIR));
            return;
        }

        for (Material other : materials) {
            final ItemStack bukkitStack = new ItemStack(other);
            final CraftItemStack craftStack = CraftItemStack.asCraftCopy(bukkitStack);
            final CraftMetaItem otherMeta = (CraftMetaItem) factory.asMetaFor(baseMeta, other);

            final String testName = name(material, other);

            if (otherMeta == null) {
                assertThat(testName, other, is(Material.AIR));
                continue;
            }

            assertTrue(testName, factory.isApplicable(otherMeta, craftStack));
            assertTrue(testName, factory.isApplicable(otherMeta, bukkitStack));
            assertTrue(testName, factory.isApplicable(otherMeta, other));
            assertTrue(testName, otherMeta.applicableTo(other));
        }
    }

    @Test
    public void blankEqualities() {
        if (material == Material.AIR) {
            return;
        }
        final CraftMetaItem baseMeta = (CraftMetaItem) factory.getItemMeta(material);
        final CraftMetaItem baseMetaClone = baseMeta.clone();

        final ItemStack baseMetaStack = new ItemStack(material);
        baseMetaStack.setItemMeta(baseMeta);

        assertThat(baseMeta, is(not(sameInstance(baseMetaStack.getItemMeta()))));

        assertTrue(factory.equals(baseMeta, null));
        assertTrue(factory.equals(null, baseMeta));

        assertTrue(factory.equals(baseMeta, baseMetaClone));
        assertTrue(factory.equals(baseMetaClone, baseMeta));

        assertThat(baseMeta, is(not(sameInstance(baseMetaClone))));

        assertThat(baseMeta, is(baseMetaClone));
        assertThat(baseMetaClone, is(baseMeta));

        for (Material other : materials) {
            final String testName = name(material, other);

            final CraftMetaItem otherMeta = (CraftMetaItem) factory.asMetaFor(baseMetaClone, other);

            if (otherMeta == null) {
                assertThat(testName, other, is(Material.AIR));
                continue;
            }

            assertTrue(testName, factory.equals(baseMeta, otherMeta));
            assertTrue(testName, factory.equals(otherMeta, baseMeta));

            assertThat(testName, baseMeta, is(otherMeta));
            assertThat(testName, otherMeta, is(baseMeta));

            assertThat(testName, baseMeta.hashCode(), is(otherMeta.hashCode()));
        }
    }
}
