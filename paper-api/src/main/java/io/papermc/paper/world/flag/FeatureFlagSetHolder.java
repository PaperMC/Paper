package io.papermc.paper.world.flag;

import java.util.Set;
import org.bukkit.FeatureFlag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Implemented by types that hold {@link FeatureFlag FeatureFlags} like
 * {@link org.bukkit.generator.WorldInfo} and {@link org.bukkit.RegionAccessor}.
 */
@NullMarked
@ApiStatus.NonExtendable
public interface FeatureFlagSetHolder {

    /**
     * Checks if this is enabled based on the loaded feature flags.
     *
     * @return true if enabled
     */
    default boolean isEnabled(final FeatureDependant featureDependant) {
        return this.getFeatureFlags().containsAll(featureDependant.requiredFeatures());
    }

    /**
     * Get all {@link FeatureFlag FeatureFlags} enabled in this world.
     *
     * @return all enabled {@link FeatureFlag FeatureFlags}
     */
    @Unmodifiable Set<FeatureFlag> getFeatureFlags();
}
