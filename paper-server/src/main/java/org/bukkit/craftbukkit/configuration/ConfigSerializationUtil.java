package org.bukkit.craftbukkit.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.CraftRegistry;

/**
 * Utilities related to the serialization and deserialization of {@link ConfigurationSerializable}s.
 */
public final class ConfigSerializationUtil {

    public static String getString(Map<?, ?> map, String key, boolean nullable) {
        return getObject(String.class, map, key, nullable);
    }

    public static UUID getUuid(Map<?, ?> map, String key, boolean nullable) {
        String uuidString = getString(map, key, nullable);
        if (uuidString == null) return null;
        return UUID.fromString(uuidString);
    }

    public static <T> T getObject(Class<T> clazz, Map<?, ?> map, String key, boolean nullable) {
        final Object object = map.get(key);
        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }
        if (object == null) {
            if (!nullable) {
                throw new NoSuchElementException(map + " does not contain " + key);
            }
            return null;
        }
        throw new IllegalArgumentException(key + "(" + object + ") is not a valid " + clazz);
    }

    public static void setHolderSet(Map<String, Object> result, String key, HolderSet<?> holders) {
        holders.unwrap()
            .ifLeft(tag -> result.put(key, "#" + tag.location().toString())) // Tag
            .ifRight(list -> result.put(key, list.stream().map((entry) -> entry.unwrapKey().orElseThrow().location().toString()).toList())); // List
    }

    public static <T> HolderSet<T> getHolderSet(Object from, ResourceKey<Registry<T>> registryKey) {
        Registry<T> registry = CraftRegistry.getMinecraftRegistry(registryKey);
        if (from instanceof String parseString && parseString.startsWith("#")) { // Tag
            parseString = parseString.substring(1);
            ResourceLocation key = ResourceLocation.tryParse(parseString);
            if (key != null) {
                Optional<HolderSet.Named<T>> tag = registry.get(TagKey.create(registryKey, key));
                if (tag.isPresent()) {
                    return tag.get();
                }
            }
        } else if (from instanceof List<?> parseList) { // List
            List<Holder.Reference<T>> holderList = new ArrayList<>(parseList.size());

            for (Object entry : parseList) {
                ResourceLocation key = ResourceLocation.tryParse(entry.toString());
                if (key == null) {
                    continue;
                }

                registry.get(key).ifPresent(holderList::add);
            }

            return HolderSet.direct(holderList);
        } else {
            throw new IllegalArgumentException("(" + from + ") is not a valid String or List");
        }

        return HolderSet.empty();
    }

    private ConfigSerializationUtil() {
    }
}
