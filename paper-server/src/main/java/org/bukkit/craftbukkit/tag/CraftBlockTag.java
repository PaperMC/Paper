package org.bukkit.craftbukkit.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.server.Block;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftBlockTag implements Tag<Material> {

    private final net.minecraft.server.Tag<Block> handle;

    public CraftBlockTag(net.minecraft.server.Tag<Block> handle) {
        this.handle = handle;
    }

    @Override
    public boolean isTagged(Material item) {
        return handle.isTagged(CraftMagicNumbers.getBlock(item));
    }

    @Override
    public Set<Material> getValues() {
        return Collections.unmodifiableSet(handle.a().stream().map((block) -> CraftMagicNumbers.getMaterial(block)).collect(Collectors.toSet()));
    }
}
