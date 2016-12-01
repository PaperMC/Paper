package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityEnderChest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftEnderChest extends CraftBlockState implements EnderChest {

    private final CraftWorld world;
    private final TileEntityEnderChest chest;

    public CraftEnderChest(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        chest = (TileEntityEnderChest) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftEnderChest(final Material material, final TileEntityEnderChest te) {
        super(material);

        chest = te;
        world = null;
    }

    @Override
    public TileEntity getTileEntity() {
        return chest;
    }
}
