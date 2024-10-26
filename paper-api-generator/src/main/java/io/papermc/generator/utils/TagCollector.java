package io.papermc.generator.utils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import io.papermc.generator.Main;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.tags.TagKey;
import org.slf4j.Logger;

// collect all the tags by grabbing the json from the data-packs
// another (probably) way is to hook into the data generator like the typed keys generator
public final class TagCollector {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static Map<TagKey<?>, String> grabExperimental(final MultiPackResourceManager resourceManager) {
        Map<TagKey<?>, String> result = new IdentityHashMap<>();

        // collect all vanilla tags
        Multimap<ResourceKey<? extends Registry<?>>, String> vanillaTags = HashMultimap.create();
        PackResources vanillaPack = resourceManager.listPacks()
            .filter(packResources -> packResources.packId().equals(BuiltInPackSource.VANILLA_ID))
            .findFirst()
            .orElseThrow();
        collectFromPack(vanillaPack, (entry, path) -> vanillaTags.put(entry.key(), path));

        // then distinct with other data-pack tags to know for sure newly created tags and so experimental one
        resourceManager.listPacks().forEach(pack -> {
            String packId = pack.packId();
            if (packId.equals(BuiltInPackSource.VANILLA_ID)) return;

            collectFromPack(pack, (entry, path) -> {
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

    private static void collectFromPack(PackResources pack, BiConsumer<RegistryAccess.RegistryEntry<?>, String> output) {
        Set<String> namespaces = pack.getNamespaces(PackType.SERVER_DATA);

        for (String namespace : namespaces) {
            Main.REGISTRY_ACCESS.registries().forEach(entry -> {
                // this is probably expensive but can't find another way around and data-pack loader has similar logic
                // the issue is that registry key can have parent/key (and custom folder too) but tag key can also have parent/key so parsing become a mess
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

    private TagCollector() {
    }
}
