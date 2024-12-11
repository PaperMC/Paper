package io.papermc.paper.world.flag;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.entry.RegistryEntry;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.FeatureFlag;
import org.bukkit.Keyed;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@AllFeatures
class FeatureFlagTest {

    @Test
    void testFeatureFlagParity() {
        final Set<ResourceLocation> locations = new HashSet<>();
        for (final FeatureFlag flag : FeatureFlag.ALL_FLAGS.values()) {
            locations.add(PaperAdventure.asVanilla(flag.getKey()));
        }
        FeatureFlags.REGISTRY.fromNames(locations, unknown -> {
            fail("Unknown api feature flag: " + unknown);
        });

        for (final ResourceLocation nmsFlag : allNames()) {
            assertNotNull(FeatureFlag.ALL_FLAGS.value(Key.key(nmsFlag.toString())), "can't find api flag for " + nmsFlag);
        }
    }

    @Test
    void testFeatureFlagConversion() {
        assertEquals(allNames().size(), PaperFeatureFlagProviderImpl.FLAGS.size());
        for (final FeatureFlag featureFlag : PaperFeatureFlagProviderImpl.FLAGS.keySet()) {
            final net.minecraft.world.flag.FeatureFlag nmsFlag = PaperFeatureFlagProviderImpl.FLAGS.get(featureFlag);
            final ResourceLocation nmsFlagName = FeatureFlags.REGISTRY.toNames(FeatureFlagSet.of(nmsFlag)).iterator().next();
            assertEquals(nmsFlagName.toString(), featureFlag.key().asString());
        }
    }

    static Set<ResourceLocation> allNames() {
        return FeatureFlags.REGISTRY.toNames(FeatureFlags.REGISTRY.allFlags());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static Set<RegistryKey<?>> featureFilteredRegistries() {
        final Set<RegistryKey<?>> registryKeys = new HashSet<>();
        for (final ResourceKey filteredRegistry : FeatureElement.FILTERED_REGISTRIES) {
            registryKeys.add(PaperRegistries.registryFromNms(filteredRegistry));
        }
        return registryKeys;
    }

    @MethodSource("featureFilteredRegistries")
    @ParameterizedTest
    <T extends Keyed> void testApiImplementsFeatureDependant(final RegistryKey<T> registryKey) {
        final org.bukkit.Registry<T> registry = RegistryAccess.registryAccess().getRegistry(registryKey);
        final T anyElement = registry.iterator().next();
        assertInstanceOf(FeatureDependant.class, anyElement, "Registry " + registryKey + " doesn't have feature dependent elements");
        final FeatureDependant dependant = ((FeatureDependant) anyElement);
        assertDoesNotThrow(dependant::requiredFeatures, "Failed to get required features for " + anyElement + " in " + registryKey);
    }

    static Stream<RegistryKey<?>> nonFeatureFilteredRegistries() {
        return RegistryHelper.getRegistry().registries().filter(r -> {
            final RegistryEntry<?, ?> entry = PaperRegistries.getEntry(r.key());
            // has an API registry and isn't a filtered registry
            return entry != null && !FeatureElement.FILTERED_REGISTRIES.contains(r.key());
        }).map(r -> PaperRegistries.getEntry(r.key()).apiKey());
    }


    @MethodSource("nonFeatureFilteredRegistries")
    @ParameterizedTest
    <T extends Keyed> void testApiDoesntImplementFeatureDependant(final RegistryKey<T> registryKey) {
        final org.bukkit.Registry<T> registry = RegistryAccess.registryAccess().getRegistry(registryKey);
        final T anyElement = registry.iterator().next();
        assertFalse(anyElement instanceof FeatureDependant, "Registry " + registryKey + " has feature dependent elements");
    }
}
