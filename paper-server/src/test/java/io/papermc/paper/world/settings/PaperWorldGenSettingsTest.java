package io.papermc.paper.world.settings;

import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@Normal
class PaperWorldGenSettingsTest {

    private static final Identifier DIMENSION_ID = Identifier.parse("test:dimension");
    private static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION_ID);

    @Test
    void returnsOriginalSettingsWithoutOverride() {
        final WorldGenSettings settings = settings(123L, true, false);

        final WorldGenSettings result = PaperWorldGenSettings.applySeedOverride(settings, DIMENSION_KEY, false, false, Map.of());

        assertSame(settings, result);
    }

    @Test
    void overridesOnlySeedForNewDimension() {
        final WorldGenSettings settings = settings(123L, false, true);

        final WorldGenSettings result = PaperWorldGenSettings.applySeedOverride(settings, DIMENSION_KEY, false, false, Map.of(DIMENSION_ID, 456L));

        assertEquals(456L, result.options().seed());
        assertFalse(result.options().generateStructures());
        assertTrue(result.options().generateBonusChest());
        assertSame(settings.dimensions(), result.dimensions());
    }

    @Test
    void doesNotOverrideExistingDimensionByDefault() {
        final WorldGenSettings settings = settings(123L, true, false);

        final WorldGenSettings result = PaperWorldGenSettings.applySeedOverride(settings, DIMENSION_KEY, true, false, Map.of(DIMENSION_ID, 456L));

        assertSame(settings, result);
    }

    @Test
    void overridesExistingDimensionWhenEnabled() {
        final WorldGenSettings settings = settings(123L, true, false);

        final WorldGenSettings result = PaperWorldGenSettings.applySeedOverride(settings, DIMENSION_KEY, true, true, Map.of(DIMENSION_ID, 456L));

        assertEquals(456L, result.options().seed());
    }

    private static WorldGenSettings settings(final long seed, final boolean generateStructures, final boolean generateBonusChest) {
        return new WorldGenSettings(
            new WorldOptions(seed, generateStructures, generateBonusChest),
            mock(WorldDimensions.class)
        );
    }
}
