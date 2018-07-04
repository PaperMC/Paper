package net.minecraft.server;

public interface KeyedObject {
    MinecraftKey getMinecraftKey();
    default String getMinecraftKeyString() {
        MinecraftKey key = getMinecraftKey();
        return key != null ? key.toString() : null;
    }
}
