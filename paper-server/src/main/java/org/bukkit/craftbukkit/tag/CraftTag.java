package org.bukkit.craftbukkit.tag;

import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.Tags;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public abstract class CraftTag<N, B extends Keyed> implements Tag<B> {

    private final net.minecraft.tags.Tags<N> registry;
    private final MinecraftKey tag;
    //
    private net.minecraft.tags.Tag<N> handle;

    public CraftTag(Tags<N> registry, MinecraftKey tag) {
        this.registry = registry;
        this.tag = tag;
    }

    protected net.minecraft.tags.Tag<N> getHandle() {
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
