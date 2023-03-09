package org.bukkit;

// Paper start - overhaul FeatureFlag API
import com.google.common.base.Preconditions;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.Index;
import org.intellij.lang.annotations.Subst;
// Paper end - overhaul FeatureFlag API
import org.jetbrains.annotations.ApiStatus;

/**
 * This represents a Feature Flag for a {@link io.papermc.paper.world.flag.FeatureFlagSetHolder}.
 */
public interface FeatureFlag extends Keyed {

    // Paper start - overhaul FeatureFlag API
    /**
     * The {@code vanilla} feature flag.
     */
    FeatureFlag VANILLA = create("vanilla");

    /**
     * <strong>AVAILABLE BETWEEN VERSIONS:</strong> 1.19.3 - 1.21.1
     *
     * @deprecated not available since 1.21.2
     */
    @Deprecated(since = "1.21.2")
    FeatureFlag BUNDLE = deprecated("bundle");

    /**
     * <strong>AVAILABLE BETWEEN VERSIONS:</strong> 1.19 - 1.19.4
     *
     * @deprecated not available since 1.20
     * The {@code trade_rebalance} feature flag.
     */
    @Deprecated(since = "1.20")
    FeatureFlag UPDATE_1_20 = deprecated("update_1_20");

    @ApiStatus.Experimental // Paper - add missing annotation
    FeatureFlag TRADE_REBALANCE = create("trade_rebalance");

    /**
     * <strong>AVAILABLE BETWEEN VERSIONS:</strong> 1.20.5 - 1.20.6
     *
     * @deprecated not available since 1.21
     */
    @Deprecated(since = "1.21")
    FeatureFlag UPDATE_121 = deprecated("update_1_21");

    /**
     * <strong>AVAILABLE BETWEEN VERSIONS:</strong> 1.21.2 - 1.21.3
     *
     * @deprecated not available since 1.21.4
     */
    @Deprecated(since = "1.21.4")
    FeatureFlag WINTER_DROP = deprecated("winter_drop");

    @ApiStatus.Experimental // Paper - add missing annotation
    FeatureFlag REDSTONE_EXPERIMENTS = create("redstone_experiments");

    @ApiStatus.Experimental // Paper - add missing annotation
    FeatureFlag MINECART_IMPROVEMENTS = create("minecart_improvements");

    /**
     * An index of all feature flags.
     */
    Index<Key, FeatureFlag> ALL_FLAGS = Index.create(FeatureFlag::key, List.copyOf(FeatureFlagImpl.ALL_FLAGS));

    private static FeatureFlag create(@Subst("vanilla") final String name) {
        final FeatureFlag flag = new FeatureFlagImpl(NamespacedKey.minecraft(name));
        Preconditions.checkState(FeatureFlagImpl.ALL_FLAGS.add(flag), "Tried to add duplicate feature flag: " + name);
        return flag;
    }

    private static FeatureFlag deprecated(@Subst("vanilla") final String name) {
        return new FeatureFlagImpl.Deprecated(NamespacedKey.minecraft(name));
    }
    // Paper end - overhaul FeatureFlag API

}
