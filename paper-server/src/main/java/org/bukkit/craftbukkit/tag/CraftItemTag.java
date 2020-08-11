package org.bukkit.craftbukkit.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.server.Item;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.Tags;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftItemTag extends CraftTag<Item, Material> {

    public CraftItemTag(Tags<Item> registry, MinecraftKey tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(Material item) {
        return getHandle().isTagged(CraftMagicNumbers.getItem(item));
    }

    @Override
    public Set<Material> getValues() {
        return Collections.unmodifiableSet(getHandle().getTagged().stream().map((item) -> CraftMagicNumbers.getMaterial(item)).collect(Collectors.toSet()));
    }
}
