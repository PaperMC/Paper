package org.bukkit.craftbukkit.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CraftNamespacedKey {

    public CraftNamespacedKey() {
    }

    public static NamespacedKey fromString(String string) {
        return CraftNamespacedKey.fromMinecraft(Identifier.parse(string));
    }

    public static NamespacedKey fromMinecraft(Identifier minecraft) {
        return new NamespacedKey(minecraft.getNamespace(), minecraft.getPath());
    }

    public static Identifier toMinecraft(NamespacedKey key) {
        return Identifier.fromNamespaceAndPath(key.getNamespace(), key.getKey());
    }

    public static NamespacedKey fromResourceKey(final ResourceKey<?> key) {
        return CraftNamespacedKey.fromMinecraft(key.identifier());
    }

    public static <T> ResourceKey<T> toResourceKey(
            final ResourceKey<? extends net.minecraft.core.Registry<T>> registry,
            final NamespacedKey namespacedKey
    ) {
        return ResourceKey.create(registry, CraftNamespacedKey.toMinecraft(namespacedKey));
    }

}
