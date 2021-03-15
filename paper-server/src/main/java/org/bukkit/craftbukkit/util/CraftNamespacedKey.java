package org.bukkit.craftbukkit.util;

import net.minecraft.resources.MinecraftKey;
import org.bukkit.NamespacedKey;

public final class CraftNamespacedKey {

    public CraftNamespacedKey() {
    }

    public static NamespacedKey fromStringOrNull(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        MinecraftKey minecraft = MinecraftKey.a(string);
        return (minecraft == null) ? null : fromMinecraft(minecraft);
    }

    public static NamespacedKey fromString(String string) {
        return fromMinecraft(new MinecraftKey(string));
    }

    public static NamespacedKey fromMinecraft(MinecraftKey minecraft) {
        return new NamespacedKey(minecraft.getNamespace(), minecraft.getKey());
    }

    public static MinecraftKey toMinecraft(NamespacedKey key) {
        return new MinecraftKey(key.getNamespace(), key.getKey());
    }
}
