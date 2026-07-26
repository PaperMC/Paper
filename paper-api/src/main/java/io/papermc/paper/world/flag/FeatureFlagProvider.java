package io.papermc.paper.world.flag;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import org.bukkit.FeatureFlag;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
interface FeatureFlagProvider {

    Optional<FeatureFlagProvider> PROVIDER = ServiceLoader.load(FeatureFlagProvider.class, FeatureFlagProvider.class.getClassLoader()).findFirst();

    static FeatureFlagProvider provider() {
        return PROVIDER.orElseThrow();
    }

    Set<FeatureFlag> requiredFeatures(FeatureDependant dependant);
}
