package io.papermc.paper.world.flag;

import java.util.Set;
import org.bukkit.FeatureFlag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Implemented by types in built-in registries that are controlled by {@link FeatureFlag FeatureFlags}.
 * Types in data-driven registries that are controlled by feature flags just will not exist in that registry.
 * @apiNote When a type that currently implements this interface transitions to being data-drive, this
 * interface will be removed from that type in the following major version.
 */
@NullMarked
@ApiStatus.NonExtendable
public interface FeatureDependant {

    /**
     * Gets the set of required feature flags for this
     * to be enabled.
     *
     * @return the immutable set of feature flags
     */
    default @Unmodifiable Set<FeatureFlag> requiredFeatures() {
        return FeatureFlagProvider.provider().requiredFeatures(this);
    }
}
