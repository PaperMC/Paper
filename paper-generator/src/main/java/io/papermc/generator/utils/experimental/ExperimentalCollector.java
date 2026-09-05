package io.papermc.generator.utils.experimental;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import io.papermc.generator.Main;
import io.papermc.generator.utils.Formatting;
import it.unimi.dsi.fastutil.Pair;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.TradeRebalanceRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;

@NullMarked
public final class ExperimentalCollector {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<ResourceKey<? extends Registry<?>>, List<RegistrySetBuilder.RegistryStub>> VANILLA_REGISTRY_ENTRIES = VanillaRegistries.WORLD_BUILDER.entries.stream()
        .flatMap(s -> s.requiredRegistries().map(k -> Pair.of(k, s)))
        .collect(Collectors.groupingBy(Pair::key, Collectors.mapping(Pair::value, Collectors.toList())));

    private static final Map<RegistrySetBuilder, SingleFlagHolder> EXPERIMENTAL_REGISTRY_FLAGS = Map.of(
        // Update for Experimental API
        TradeRebalanceRegistries.WORLD_BUILDER, FlagHolders.TRADE_REBALANCE
    );

    private static final Multimap<ResourceKey<? extends Registry<?>>, Map.Entry<SingleFlagHolder, RegistrySetBuilder.RegistryStub>> EXPERIMENTAL_REGISTRY_ENTRIES;

    static {
        EXPERIMENTAL_REGISTRY_ENTRIES = HashMultimap.create();
        for (final Map.Entry<RegistrySetBuilder, SingleFlagHolder> entry : EXPERIMENTAL_REGISTRY_FLAGS.entrySet()) {
            for (final RegistrySetBuilder.RegistryStub stub : entry.getKey().entries) {
                stub.requiredRegistries().forEach(registry -> EXPERIMENTAL_REGISTRY_ENTRIES.put(registry, Map.entry(entry.getValue(), stub)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<ResourceKey<T>, SingleFlagHolder> collectDataDrivenElementIds(final Registry<T> registry) {
        final Collection<Map.Entry<SingleFlagHolder, RegistrySetBuilder.RegistryStub>> experimentalEntries = EXPERIMENTAL_REGISTRY_ENTRIES.get(registry.key());
        if (experimentalEntries.isEmpty()) {
            return Collections.emptyMap();
        }

        final HolderLookup.Provider staticRegistries = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        final HolderLookup.Provider vanillaWorldAccess = VanillaRegistries.createWorldLookup();

        final Map<ResourceKey<T>, SingleFlagHolder> result = new IdentityHashMap<>();
        for (final Map.Entry<SingleFlagHolder, RegistrySetBuilder.RegistryStub> entry : experimentalEntries) {
            final RegistrySetBuilder.BootstrappedRegistryState<?> registryAdditions = getRegistryAdditions(registry, entry.getValue(), staticRegistries, vanillaWorldAccess);
            result.putAll(registryAdditions.registeredValues().keySet().stream().collect(Collectors.toMap(k -> (ResourceKey<T>) k, _ -> entry.getKey())));
        }

        final List<RegistrySetBuilder.RegistryStub> vanillaStubs = VANILLA_REGISTRY_ENTRIES.get(registry.key());
        if (vanillaStubs == null || vanillaStubs.isEmpty()) return result;

        for (final RegistrySetBuilder.RegistryStub stub : vanillaStubs) {
            final RegistrySetBuilder.BootstrappedRegistryState<T> registryAdditions = getRegistryAdditions(registry, stub, staticRegistries, vanillaWorldAccess);
            registryAdditions.registeredValues().keySet().forEach(result::remove);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T> RegistrySetBuilder.@UnknownNullability BootstrappedRegistryState<T> getRegistryAdditions(
        final Registry<T> registry,
        final RegistrySetBuilder.RegistryStub stub,
        final HolderLookup.Provider staticRegistries,
        final HolderLookup.Provider vanillaWorldAccess
    ) {
        final Set<ResourceKey<? extends Registry<?>>> registriesMissingFromPatch = RegistrySetBuilder.findRegistriesMissingFromPatch(
            staticRegistries,
            vanillaWorldAccess,
            List.of(stub)
        );
        final List<RegistrySetBuilder.RegistryStub> stubs = Stream.concat(
            Stream.of(stub),
            registriesMissingFromPatch.stream().map(RegistrySetBuilder::placeholderStub)
        ).toList();

        final RegistrySetBuilder.BuildState buildState = RegistrySetBuilder.BuildState.createAndApply(staticRegistries, stubs);
        return (RegistrySetBuilder.BootstrappedRegistryState<T>) buildState.bootstrappedRegistries().get(registry.key());
    }

    // collect all the tags by grabbing the json from the data-packs
    // another (probably) way is to hook into the data generator like the typed keys generator
    public static Map<TagKey<?>, String> collectTags(final ResourceManager resourceManager) {
        final Map<TagKey<?>, String> result = new IdentityHashMap<>();

        // collect all vanilla tags
        final Multimap<ResourceKey<? extends Registry<?>>, String> vanillaTags = HashMultimap.create();
        final PackResources vanillaPack = resourceManager.listPacks()
            .filter(packResources -> packResources.packId().equals(BuiltInPackSource.VANILLA_ID))
            .findFirst()
            .orElseThrow();
        collectTagsFromPack(vanillaPack, (entry, path) -> vanillaTags.put(entry.key(), path));

        // then distinct with other data-pack tags to know for sure newly created tags and so experimental one
        resourceManager.listPacks().forEach(pack -> {
            final String packId = pack.packId();
            if (packId.equals(BuiltInPackSource.VANILLA_ID)) return;

            collectTagsFromPack(pack, (entry, path) -> {
                if (vanillaTags.get(entry.key()).contains(path)) {
                    return;
                }

                result.put(entry.value().listTagIds()
                    .filter(tagKey -> tagKey.location().getPath().equals(path))
                    .findFirst()
                    .orElseThrow(), packId);
            });
        });
        return Collections.unmodifiableMap(result);
    }

    private static void collectTagsFromPack(final PackResources pack, final BiConsumer<RegistryAccess.RegistryEntry<?>, String> output) {
        final Set<String> namespaces = pack.getNamespaces(PackType.SERVER_DATA);

        for (final String namespace : namespaces) {
            Main.REGISTRY_ACCESS.registries().forEach(entry -> {
                // this is probably expensive but can't find another way around and data-pack loader has similar logic
                // the issue is that registry key can have parent/key but tag key can also have parent/key so parsing become a mess
                // without having at least one of the two values
                final String tagDir = Registries.tagsDirPath(entry.key());
                pack.listResources(PackType.SERVER_DATA, namespace, tagDir, (id, supplier) -> {
                    Formatting.formatTagKey(tagDir, id.getPath()).ifPresentOrElse(path -> output.accept(entry, path), () -> {
                        LOGGER.warn("Unable to parse the path: {}/{}/{}.json in the data-pack {} into a tag key", namespace, tagDir, id.getPath(), pack.packId());
                    });
                });
            });
        }
    }

    private ExperimentalCollector() {
    }
}
