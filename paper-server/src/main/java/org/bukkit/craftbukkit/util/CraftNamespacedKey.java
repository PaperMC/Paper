package org.bukkit.craftbukkit.util;

import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;

public final class CraftNamespacedKey {

    public CraftNamespacedKey() {
    }

    public static NamespacedKey fromStringOrNull(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        ResourceLocation minecraft = ResourceLocation.tryParse(string);
        return (minecraft == null) ? null : CraftNamespacedKey.fromMinecraft(minecraft);
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
