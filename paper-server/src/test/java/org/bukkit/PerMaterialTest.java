package org.bukkit;

import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
