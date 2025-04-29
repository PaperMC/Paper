package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.core.component.DataComponents;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.material.MaterialData;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@AllFeatures
public class PerMaterialTest {

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    @Disabled
    public void isTransparent(Material material) {
        if (material == Material.AIR) {
            assertTrue(material.isTransparent());
        } else if (material.isBlock()) {
            // assertThat(material.isTransparent(), is(not(CraftMagicNumbers.getBlock(material).getBlockData().getMaterial().blocksLight()))); // PAIL: not unit testable anymore (17w50a)
        } else {
            assertFalse(material.isTransparent());
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void usesDurability(Material material) {
        if (!material.isBlock()) {
            assertThat(EnchantmentTarget.BREAKABLE.includes(material), is(CraftMagicNumbers.getItem(material).components().getOrDefault(DataComponents.MAX_DAMAGE, 0) > 0));
        } else {
            assertFalse(EnchantmentTarget.BREAKABLE.includes(material));
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testBlockDataCreation(Material material) {
        if (material.isBlock()) {
            assertNotNull(material.createBlockData());
        }
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = "LEGACY_.*", mode = EnumSource.Mode.MATCH_NONE)
    public void testBlockDataClass(Material material) {
        if (material.isBlock()) {
            Class<?> expectedClass = material.data;
            if (expectedClass != MaterialData.class) {
                BlockData blockData = Bukkit.createBlockData(material);
                assertTrue(expectedClass.isInstance(blockData), expectedClass + " <> " + blockData.getClass());
            }
        }
    }
}
