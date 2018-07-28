package org.bukkit.craftbukkit.tag;

import net.minecraft.server.MinecraftKey;
import net.minecraft.server.TagsServer;
import org.bukkit.Keyed;
import org.bukkit.Tag;

public abstract class CraftTag<N, B extends Keyed> implements Tag<B> {

    private final net.minecraft.server.TagsServer<N> registry;
    private final MinecraftKey tag;
    //
    private int version = -1;
    private net.minecraft.server.Tag<N> handle;

    public CraftTag(TagsServer<N> registry, MinecraftKey tag) {
        this.registry = registry;
        this.tag = tag;
    }

    protected net.minecraft.server.Tag<N> getHandle() {
        if (version != registry.version) {
            handle = registry.b(tag);
            version = registry.version;
        }

        return handle;
    }
}
