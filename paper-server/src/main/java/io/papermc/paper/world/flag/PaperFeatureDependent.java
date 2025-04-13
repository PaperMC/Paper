package io.papermc.paper.world.flag;

import io.papermc.paper.util.Holderable;
import java.util.Set;
import net.minecraft.world.flag.FeatureElement;
import org.bukkit.FeatureFlag;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PaperFeatureDependent<M extends FeatureElement> extends FeatureDependant, Holderable<M> {

    @Override
    default @Unmodifiable Set<FeatureFlag> requiredFeatures() {
        return PaperFeatureFlagProviderImpl.fromNms(this.getHandle().requiredFeatures());
    }
}
