package org.bukkit.craftbukkit.tag;

import net.minecraft.core.HolderSet;
import net.minecraft.core.IRegistry;
import net.minecraft.tags.TagKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public abstract class CraftTag<N, B extends Keyed> implements Tag<B> {

    protected final IRegistry<N> registry;
    protected final TagKey<N> tag;
    //
    private HolderSet.Named<N> handle;

    public CraftTag(IRegistry<N> registry, TagKey<N> tag) {
        this.registry = registry;
        this.tag = tag;
    }

    protected HolderSet.Named<N> getHandle() {
        if (handle == null) {
            handle = registry.getTag(tag).get();
        }

        return handle;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(tag.location());
    }
}
