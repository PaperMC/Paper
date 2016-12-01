package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityComparator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftComparator extends CraftBlockState implements EnderChest {

    private final CraftWorld world;
    private final TileEntityComparator comparator;

    public CraftComparator(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        comparator = (TileEntityComparator) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftComparator(final Material material, final TileEntityComparator te) {
        super(material);

        comparator = te;
        world = null;
    }

    @Override
    public TileEntity getTileEntity() {
        return comparator;
    }
}
