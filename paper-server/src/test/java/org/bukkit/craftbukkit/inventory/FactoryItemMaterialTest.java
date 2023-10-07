package org.bukkit.craftbukkit.inventory;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@EnabledIfSystemProperty(named = "testEnv", matches = "full", disabledReason = "Disable for now, since Mockito's Location feature is too heavy in combination with this test")
public class FactoryItemMaterialTest extends AbstractTestingBase {
    static final ItemFactory factory = CraftItemFactory.instance();
    static final StringBuilder buffer = new StringBuilder();
    static final Material[] materials;

    static {
        Material[] local_materials = Material.values();
        List<Material> list = new ArrayList<Material>(local_materials.length);
        for (Material material : local_materials) {
            if (INVALIDATED_MATERIALS.contains(material)) {
                continue;
            }

            list.add(material);
        }
        materials = list.toArray(new Material[list.size()]);
    }

    static String name(Enum<?> from, Enum<?> to) {
        if (from.getClass() == to.getClass()) {
            return buffer.delete(0, Integer.MAX_VALUE).append(from.getClass().getName()).append(' ').append(from.name()).append(" to ").append(to.name()).toString();
        }
        return buffer.delete(0, Integer.MAX_VALUE).append(from.getClass().getName()).append('(').append(from.name()).append(") to ").append(to.getClass().getName()).append('(').append(to.name()).append(')').toString();
    }

    public static Stream<Arguments> data() {
        List<Arguments> list = new ArrayList<>();
        for (Material material : materials) {
            list.add(Arguments.of(material));
        }
        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("data")
    public void itemStack(Material material) {
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

    @ParameterizedTest
    @MethodSource("data")
    public void generalCase(Material material) {
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

    @ParameterizedTest
    @MethodSource("data")
    public void asMetaFor(Material material) {
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
                assertThat(other, is(Material.AIR), testName);
                continue;
            }

            assertTrue(factory.isApplicable(otherMeta, craftStack), testName);
            assertTrue(factory.isApplicable(otherMeta, bukkitStack), testName);
            assertTrue(factory.isApplicable(otherMeta, other), testName);
            assertTrue(otherMeta.applicableTo(other), testName);
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    public void blankEqualities(Material material) {
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
                assertThat(other, is(Material.AIR), testName);
                continue;
            }

            assertTrue(factory.equals(baseMeta, otherMeta), testName);
            assertTrue(factory.equals(otherMeta, baseMeta), testName);

            assertThat(baseMeta, is(otherMeta), testName);
            assertThat(otherMeta, is(baseMeta), testName);

            assertThat(baseMeta.hashCode(), is(otherMeta.hashCode()), testName);
        }
    }
}
