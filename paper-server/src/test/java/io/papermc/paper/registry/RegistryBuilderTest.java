package io.papermc.paper.registry;

import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.entry.RegistryEntry;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
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
            .map(RegistryEntry::meta)
            .filter(RegistryEntryMeta.Buildable.class::isInstance)
            .map(Arguments::arguments);
    }

    @ParameterizedTest
    @MethodSource("registries")
    <M, T extends Keyed> void testEquality(final RegistryEntryMeta.Buildable<M, T, ?> registryEntry) { // TODO remove Keyed
        final Registry<M> registry = RegistryHelper.getRegistry().lookupOrThrow(registryEntry.mcKey());
        for (final Map.Entry<ResourceKey<M>, M> entry : registry.entrySet()) {
            final M built = registryEntry.builderFiller().fill(Conversions.global(), entry.getValue()).build();
            assertEquals(entry.getValue(), built);
        }
    }
}
