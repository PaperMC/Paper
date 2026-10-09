package io.papermc.paper.world.settings;

import io.papermc.paper.configuration.GlobalConfiguration;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperWorldGenSettings {

    private PaperWorldGenSettings() {
    }

    public static WorldGenSettings applySeedOverride(
        final WorldGenSettings settings,
        final ResourceKey<Level> dimensionKey,
        final boolean existingDimension
    ) {
        final GlobalConfiguration.WorldGeneration configuration = GlobalConfiguration.get().worldGeneration;
        return applySeedOverride(
            settings,
            dimensionKey,
            existingDimension,
            configuration.applyToExistingDimensions,
            configuration.dimensionSeedOverrides
        );
    }

    static WorldGenSettings applySeedOverride(
        final WorldGenSettings settings,
        final ResourceKey<Level> dimensionKey,
        final boolean existingDimension,
        final boolean applyToExistingDimensions,
        final Map<Identifier, Long> seedOverrides
    ) {
        if (existingDimension && !applyToExistingDimensions) {
            return settings;
        }

        final Long seedOverride = seedOverrides.get(dimensionKey.identifier());
        if (seedOverride == null) {
            return settings;
        }

        final WorldOptions options = settings.options();
        return new WorldGenSettings(
            new WorldOptions(seedOverride, options.generateStructures(), options.generateBonusChest()),
            settings.dimensions()
        );
    }
}
