package org.bukkit.craftbukkit.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.server.Block;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.Tags;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftBlockTag extends CraftTag<Block, Material> {

    public CraftBlockTag(Tags<Block> registry, MinecraftKey tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(Material item) {
        return getHandle().isTagged(CraftMagicNumbers.getBlock(item));
    }

    @Override
    public Set<Material> getValues() {
        return Collections.unmodifiableSet(getHandle().getTagged().stream().map((block) -> CraftMagicNumbers.getMaterial(block)).collect(Collectors.toSet()));
    }
}
