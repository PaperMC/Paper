package org.bukkit.craftbukkit.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.server.Item;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftItemTag implements Tag<Material> {

    private final net.minecraft.server.Tag<Item> handle;

    public CraftItemTag(net.minecraft.server.Tag<Item> handle) {
        this.handle = handle;
    }

    @Override
    public boolean isTagged(Material item) {
        return handle.isTagged(CraftMagicNumbers.getItem(item));
    }

    @Override
    public Set<Material> getValues() {
        return Collections.unmodifiableSet(handle.a().stream().map((item) -> CraftMagicNumbers.getMaterial(item)).collect(Collectors.toSet()));
    }
}
