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
import org.bukkit.support.LegacyHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@AllFeatures
@EnabledIfSystemProperty(named = "testEnv", matches = "full", disabledReason = "Disable for now, since Mockito's Location feature is too heavy in combination with this test")
public class FactoryItemMaterialTest {
    static final ItemFactory factory = CraftItemFactory.instance();
    static final StringBuilder buffer = new StringBuilder();
    static final Material[] materials;

    static {
        Material[] local_materials = Material.values();
        List<Material> list = new ArrayList<Material>(local_materials.length);
        for (Material material : local_materials) {
            if (LegacyHelper.getInvalidatedMaterials().contains(material)) {
                continue;
            }

            list.add(material);
        }
        materials = list.toArray(new Material[list.size()]);
    }

    static String name(Enum<?> from, Enum<?> to) {
        if (from.getClass() == to.getClass()) {
            return FactoryItemMaterialTest.buffer.delete(0, Integer.MAX_VALUE).append(from.getClass().getName()).append(' ').append(from.name()).append(" to ").append(to.name()).toString();
        }
        return FactoryItemMaterialTest.buffer.delete(0, Integer.MAX_VALUE).append(from.getClass().getName()).append('(').append(from.name()).append(") to ").append(to.getClass().getName()).append('(').append(to.name()).append(')').toString();
    }

    public static Stream<Arguments> data() {
        List<Arguments> list = new ArrayList<>();
        for (Material material : FactoryItemMaterialTest.materials) {
            list.add(Arguments.of(material));
        }
        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("data")
    public void itemStack(Material material) {
        ItemStack bukkitStack = new ItemStack(material);
        CraftItemStack craftStack = CraftItemStack.asCraftCopy(bukkitStack);
        ItemMeta meta = FactoryItemMaterialTest.factory.getItemMeta(material);
        if (meta == null) {
            assertThat(material, is(Material.AIR));
        } else {
            assertTrue(FactoryItemMaterialTest.factory.isApplicable(meta, bukkitStack));
            assertTrue(FactoryItemMaterialTest.factory.isApplicable(meta, craftStack));
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    public void generalCase(Material material) {
        CraftMetaItem meta = (CraftMetaItem) FactoryItemMaterialTest.factory.getItemMeta(material);
        if (meta == null) {
            assertThat(material, is(Material.AIR));
        } else {
            assertTrue(FactoryItemMaterialTest.factory.isApplicable(meta, material));
            assertTrue(meta.applicableTo(material));

            meta = meta.clone();
            assertTrue(FactoryItemMaterialTest.factory.isApplicable(meta, material));
            assertTrue(meta.applicableTo(material));
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    public void asMetaFor(Material material) {
        final CraftMetaItem baseMeta = (CraftMetaItem) FactoryItemMaterialTest.factory.getItemMeta(material);
        if (baseMeta == null) {
            assertThat(material, is(Material.AIR));
            return;
        }

        for (Material other : FactoryItemMaterialTest.materials) {
            final ItemStack bukkitStack = new ItemStack(other);
            final CraftItemStack craftStack = CraftItemStack.asCraftCopy(bukkitStack);
            final CraftMetaItem otherMeta = (CraftMetaItem) FactoryItemMaterialTest.factory.asMetaFor(baseMeta, other);

            final String testName = FactoryItemMaterialTest.name(material, other);

            if (otherMeta == null) {
                assertThat(other, is(Material.AIR), testName);
                continue;
            }

            assertTrue(FactoryItemMaterialTest.factory.isApplicable(otherMeta, craftStack), testName);
            assertTrue(FactoryItemMaterialTest.factory.isApplicable(otherMeta, bukkitStack), testName);
            assertTrue(FactoryItemMaterialTest.factory.isApplicable(otherMeta, other), testName);
            assertTrue(otherMeta.applicableTo(other), testName);
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    public void blankEqualities(Material material) {
        if (material == Material.AIR) {
            return;
        }
        final CraftMetaItem baseMeta = (CraftMetaItem) FactoryItemMaterialTest.factory.getItemMeta(material);
        final CraftMetaItem baseMetaClone = baseMeta.clone();

        final ItemStack baseMetaStack = new ItemStack(material);
        baseMetaStack.setItemMeta(baseMeta);

        assertThat(baseMeta, is(not(sameInstance(baseMetaStack.getItemMeta()))));

        assertTrue(FactoryItemMaterialTest.factory.equals(baseMeta, null));
        assertTrue(FactoryItemMaterialTest.factory.equals(null, baseMeta));

        assertTrue(FactoryItemMaterialTest.factory.equals(baseMeta, baseMetaClone));
        assertTrue(FactoryItemMaterialTest.factory.equals(baseMetaClone, baseMeta));

        assertThat(baseMeta, is(not(sameInstance(baseMetaClone))));

        assertThat(baseMeta, is(baseMetaClone));
        assertThat(baseMetaClone, is(baseMeta));

        for (Material other : FactoryItemMaterialTest.materials) {
            final String testName = FactoryItemMaterialTest.name(material, other);

            final CraftMetaItem otherMeta = (CraftMetaItem) FactoryItemMaterialTest.factory.asMetaFor(baseMetaClone, other);

            if (otherMeta == null) {
                assertThat(other, is(Material.AIR), testName);
                continue;
            }

            assertTrue(FactoryItemMaterialTest.factory.equals(baseMeta, otherMeta), testName);
            assertTrue(FactoryItemMaterialTest.factory.equals(otherMeta, baseMeta), testName);

            assertThat(baseMeta, is(otherMeta), testName);
            assertThat(otherMeta, is(baseMeta), testName);

            assertThat(baseMeta.hashCode(), is(otherMeta.hashCode()), testName);
        }
    }
}
