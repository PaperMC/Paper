package io.papermc.paper.world.flag;

import java.util.Set;
import net.minecraft.world.flag.FeatureElement;
import org.bukkit.FeatureFlag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Unmodifiable;

public interface PaperFeatureDependent extends FeatureDependant {

    <M extends FeatureElement> M getHandle();

    @Override
    default @Unmodifiable @NonNull Set<FeatureFlag> requiredFeatures() {
        return PaperFeatureFlagProviderImpl.fromNms(this.getHandle().requiredFeatures());
    }
}
