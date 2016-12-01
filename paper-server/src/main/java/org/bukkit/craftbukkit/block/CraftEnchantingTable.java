package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityEnchantTable;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.EnchantingTable;

public class CraftEnchantingTable extends CraftBlockState implements EnchantingTable {

    private final CraftWorld world;
    private final TileEntityEnchantTable enchant;

    public CraftEnchantingTable(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        enchant = (TileEntityEnchantTable) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftEnchantingTable(final Material material, final TileEntityEnchantTable te) {
        super(material);

        enchant = te;
        world = null;
    }

    @Override
    public TileEntity getTileEntity() {
        return enchant;
    }

    @Override
    public String getCustomName() {
        return enchant.hasCustomName() ? enchant.getName() : null;
    }

    @Override
    public void setCustomName(String name) {
        enchant.a(name); // PAIL: setCustomName
    }
}
