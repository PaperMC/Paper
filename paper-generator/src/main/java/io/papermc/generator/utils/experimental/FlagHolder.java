package io.papermc.generator.utils.experimental;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

public interface FlagHolder {

    default FeatureFlagSet flagSet() {
        return FeatureFlags.REGISTRY.subset(this.flag());
    }

    FeatureFlag flag();
}
