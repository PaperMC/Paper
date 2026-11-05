package io.papermc.paper.world.flag;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import org.bukkit.FeatureFlag;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
interface FeatureFlagProvider {

    static FeatureFlagProvider provider() {
        final class Holder {
            static final Optional<FeatureFlagProvider> PROVIDER = ServiceLoader.load(FeatureFlagProvider.class, FeatureFlagProvider.class.getClassLoader()).findFirst();
        }

        return Holder.PROVIDER.orElseThrow();
    }

    Set<FeatureFlag> requiredFeatures(FeatureDependant dependant);
}
