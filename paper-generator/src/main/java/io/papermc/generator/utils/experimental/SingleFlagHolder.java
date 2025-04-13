package io.papermc.generator.utils.experimental;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.MinecraftExperimental;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record SingleFlagHolder(FeatureFlag flag) implements FlagHolder { // todo support multiple flags?

    private static final Map<String, FeatureFlag> FEATURE_FLAG_CACHE = new HashMap<>();
    private static final Map<FeatureFlag, ResourceLocation> FEATURE_FLAG_NAME = HashBiMap.create(FeatureFlags.REGISTRY.names).inverse();

    static SingleFlagHolder fromValue(FeatureFlag flag) {
        return new SingleFlagHolder(flag);
    }

    public static SingleFlagHolder fromSet(FeatureFlagSet standaloneSet) {
        Preconditions.checkArgument(Long.bitCount(standaloneSet.mask) == 1, "Flag set size must be equals to 1.");

        for (FeatureFlag flag : FeatureFlags.REGISTRY.names.values()) {
            if (standaloneSet.contains(flag)) {
                return fromValue(flag);
            }
        }

        throw new IllegalStateException();
    }

    public static SingleFlagHolder fromName(String name) {
        return fromValue(FEATURE_FLAG_CACHE.computeIfAbsent(name, key -> {
            return FeatureFlags.REGISTRY.names.get(ResourceLocation.withDefaultNamespace(key));
        }));
    }

    public MinecraftExperimental.Requires asAnnotationMember() {
        MinecraftExperimental.Requires annotationMember = FlagHolders.ANNOTATION_EQUIVALENT.get(this.flag);
        if (annotationMember == null) {
            throw new UnsupportedOperationException("Don't know that feature flag: " + FEATURE_FLAG_NAME.get(this.flag));
        }
        return annotationMember;
    }
}
