package io.papermc.paper.math.provider;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;

public interface IntProviderType<T extends IntProvider> extends Keyed {

    IntProviderType<IntProvider.Constant> CONSTANT = get("constant");
    IntProviderType<IntProvider.Uniform> UNIFORM = get("uniform");
    IntProviderType<IntProvider.BiasedToBottom> BIASED_TO_BOTTOM = get("biased_to_bottom");
    IntProviderType<IntProvider.Clamped> CLAMPED = get("clamped");
    IntProviderType<IntProvider.WeightedList> WEIGHTED = get("weighted_list");
    IntProviderType<IntProvider.ClampedNormal> CLAMPED_NORMAL = get("clamped_normal");

    @SuppressWarnings("unchecked")
    private static <T extends IntProvider> IntProviderType<T> get(@KeyPattern final String name) {
        return (IntProviderType<T>) RegistryAccess.registryAccess().getRegistry(RegistryKey.INT_PROVIDER_TYPE).getOrThrow(Key.key(name));
    }
}
