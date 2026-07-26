package io.papermc.generator.utils.experimental;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import io.papermc.generator.Main;
import io.papermc.generator.utils.Formatting;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.TradeRebalanceRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@NullMarked
public final class ExperimentalCollector {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<ResourceKey<? extends Registry<?>>, RegistrySetBuilder.RegistryBootstrap<?>> VANILLA_REGISTRY_ENTRIES = VanillaRegistries.BUILDER.entries.stream()
        .collect(Collectors.toMap(RegistrySetBuilder.RegistryStub::key, RegistrySetBuilder.RegistryStub::bootstrap));

    private static final Map<RegistrySetBuilder, SingleFlagHolder> EXPERIMENTAL_REGISTRY_FLAGS = Map.of(
        // Update for Experimental API
        TradeRebalanceRegistries.BUILDER, FlagHolders.TRADE_REBALANCE
    );

    private static final Multimap<ResourceKey<? extends Registry<?>>, Map.Entry<SingleFlagHolder, RegistrySetBuilder.RegistryBootstrap<?>>> EXPERIMENTAL_REGISTRY_ENTRIES;
    static {
        EXPERIMENTAL_REGISTRY_ENTRIES = HashMultimap.create();
        for (Map.Entry<RegistrySetBuilder, SingleFlagHolder> entry : EXPERIMENTAL_REGISTRY_FLAGS.entrySet()) {
            for (RegistrySetBuilder.RegistryStub<?> stub : entry.getKey().entries) {
                EXPERIMENTAL_REGISTRY_ENTRIES.put(stub.key(), Map.entry(entry.getValue(), stub.bootstrap()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<ResourceKey<T>, SingleFlagHolder> collectDataDrivenElementIds(Registry<T> registry) {
        Collection<Map.Entry<SingleFlagHolder, RegistrySetBuilder.RegistryBootstrap<?>>> experimentalEntries = EXPERIMENTAL_REGISTRY_ENTRIES.get(registry.key());
        if (experimentalEntries.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<ResourceKey<T>, SingleFlagHolder> result = new IdentityHashMap<>();
        for (Map.Entry<SingleFlagHolder, RegistrySetBuilder.RegistryBootstrap<?>> experimentalEntry : experimentalEntries) {
            RegistrySetBuilder.RegistryBootstrap<T> experimentalBootstrap = (RegistrySetBuilder.RegistryBootstrap<T>) experimentalEntry.getValue();
            Set<ResourceKey<T>> experimental = Collections.newSetFromMap(new IdentityHashMap<>());
            CollectingContext<T> experimentalCollector = new CollectingContext<>(experimental, registry);
            experimentalBootstrap.run(experimentalCollector);
            result.putAll(experimental.stream().collect(Collectors.toMap(key -> key, key -> experimentalEntry.getKey())));
        }

        RegistrySetBuilder.@Nullable RegistryBootstrap<T> vanillaBootstrap = (RegistrySetBuilder.RegistryBootstrap<T>) VANILLA_REGISTRY_ENTRIES.get(registry.key());
        if (vanillaBootstrap != null) {
            Set<ResourceKey<T>> vanilla = Collections.newSetFromMap(new IdentityHashMap<>());
            CollectingContext<T> vanillaCollector = new CollectingContext<>(vanilla, registry);
            vanillaBootstrap.run(vanillaCollector);
            result.keySet().removeAll(vanilla);
        }
        return result;
    }

    // collect all the tags by grabbing the json from the data-packs
    // another (probably) way is to hook into the data generator like the typed keys generator
    public static Map<TagKey<?>, String> collectTags(ResourceManager resourceManager) {
        Map<TagKey<?>, String> result = new IdentityHashMap<>();

        // collect all vanilla tags
        Multimap<ResourceKey<? extends Registry<?>>, String> vanillaTags = HashMultimap.create();
        PackResources vanillaPack = resourceManager.listPacks()
            .filter(packResources -> packResources.packId().equals(BuiltInPackSource.VANILLA_ID))
            .findFirst()
            .orElseThrow();
        collectTagsFromPack(vanillaPack, (entry, path) -> vanillaTags.put(entry.key(), path));

        // then distinct with other data-pack tags to know for sure newly created tags and so experimental one
        resourceManager.listPacks().forEach(pack -> {
            String packId = pack.packId();
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

    private static void collectTagsFromPack(PackResources pack, BiConsumer<RegistryAccess.RegistryEntry<?>, String> output) {
        Set<String> namespaces = pack.getNamespaces(PackType.SERVER_DATA);

        for (String namespace : namespaces) {
            Main.REGISTRY_ACCESS.registries().forEach(entry -> {
                // this is probably expensive but can't find another way around and data-pack loader has similar logic
                // the issue is that registry key can have parent/key but tag key can also have parent/key so parsing become a mess
                // without having at least one of the two values
                String tagDir = Registries.tagsDirPath(entry.key());
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
