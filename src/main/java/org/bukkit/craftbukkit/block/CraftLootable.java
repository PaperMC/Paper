package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityLootable;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftLootable extends CraftContainer implements Nameable {

    private final TileEntityLootable te;

    public CraftLootable(Block block) {
        super(block);

        te = (TileEntityLootable) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftLootable(Material material, TileEntityLootable tileEntity) {
        super(material, tileEntity);

        te = tileEntity;
    }

    @Override
    public String getCustomName() {
        return te.hasCustomName() ? te.getName() : null;
    }

    @Override
    public void setCustomName(String name) {
        te.a(name); // PAIL: setCustomName
    }
}
