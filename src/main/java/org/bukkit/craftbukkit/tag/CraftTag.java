package org.bukkit.craftbukkit.tag;

import net.minecraft.server.MinecraftKey;
import net.minecraft.server.Tags;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public abstract class CraftTag<N, B extends Keyed> implements Tag<B> {

    private final net.minecraft.server.Tags<N> registry;
    private final MinecraftKey tag;
    //
    private net.minecraft.server.Tag<N> handle;

    public CraftTag(Tags<N> registry, MinecraftKey tag) {
        this.registry = registry;
        this.tag = tag;
    }

    protected net.minecraft.server.Tag<N> getHandle() {
        if (handle == null) {
            handle = registry.b(tag);
        }

        return handle;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(tag);
    }
}
