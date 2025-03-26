package org.bukkit.craftbukkit.util;

import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class CraftNamespacedKey {

    public CraftNamespacedKey() {
    }

    public static @Nullable NamespacedKey fromStringOrNull(@Nullable String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        ResourceLocation minecraft = ResourceLocation.tryParse(string);
        return (minecraft == null || minecraft.getPath().isEmpty()) ? null : CraftNamespacedKey.fromMinecraft(minecraft); // Paper - Bukkit's parser does not match Vanilla for empty paths
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
}
