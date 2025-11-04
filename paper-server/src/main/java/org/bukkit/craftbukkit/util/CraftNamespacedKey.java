package org.bukkit.craftbukkit.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CraftNamespacedKey {

    public CraftNamespacedKey() {
    }

    public static NamespacedKey fromString(String string) {
        return CraftNamespacedKey.fromMinecraft(ResourceLocation.parse(string));
    }

    public static NamespacedKey fromMinecraft(ResourceLocation minecraft) {
        return new NamespacedKey(minecraft.getNamespace(), minecraft.getPath());
    }

    public static ResourceLocation toMinecraft(NamespacedKey key) {
        return ResourceLocation.fromNamespaceAndPath(key.getNamespace(), key.getKey());
    }

    public static NamespacedKey fromResourceKey(final ResourceKey<?> key) {
        return CraftNamespacedKey.fromMinecraft(key.location());
    }

    public static <T> ResourceKey<T> toResourceKey(
            final ResourceKey<? extends net.minecraft.core.Registry<T>> registry,
            final NamespacedKey namespacedKey
    ) {
        return ResourceKey.create(registry, CraftNamespacedKey.toMinecraft(namespacedKey));
    }

}
