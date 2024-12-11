package io.papermc.paper.registry;

import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.entry.RegistryEntry;
import io.papermc.paper.registry.entry.RegistryEntryInfo;
import io.papermc.paper.registry.legacy.DelayedRegistryEntry;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AllFeatures
class RegistryBuilderTest {

    static Stream<Arguments> registries() {
        return PaperRegistries.REGISTRY_ENTRIES.stream()
            .map(RegistryBuilderTest::possiblyUnwrap)
            .filter(RegistryEntry.BuilderHolder.class::isInstance)
            .map(Arguments::arguments);
    }

    private static <M, B extends Keyed> RegistryEntryInfo<M, B> possiblyUnwrap(final RegistryEntryInfo<M, B> entry) {
        return entry instanceof final DelayedRegistryEntry<M, B> delayed ? delayed.delegate() : entry;
    }

    @ParameterizedTest
    @MethodSource("registries")
    <M, T> void testEquality(final RegistryEntry.BuilderHolder<M, T, ?> registryEntry) {
        final Registry<M> registry = RegistryHelper.getRegistry().lookupOrThrow(registryEntry.mcKey());
        for (final Map.Entry<ResourceKey<M>, M> entry : registry.entrySet()) {
            final M built = registryEntry.fillBuilder(new Conversions(new RegistryOps.HolderLookupAdapter(RegistryHelper.getRegistry())), entry.getValue()).build();
            assertEquals(entry.getValue(), built);
        }
    }
}
