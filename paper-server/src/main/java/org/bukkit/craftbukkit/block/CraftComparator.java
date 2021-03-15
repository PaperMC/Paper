package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityComparator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Comparator;

public class CraftComparator extends CraftBlockEntityState<TileEntityComparator> implements Comparator {

    public CraftComparator(final Block block) {
        super(block, TileEntityComparator.class);
    }

    public CraftComparator(final Material material, final TileEntityComparator te) {
        super(material, te);
    }
}
