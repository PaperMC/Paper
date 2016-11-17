package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityShulkerBox;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftShulkerBox extends CraftContainer implements ShulkerBox {

    private final CraftWorld world;
    private final TileEntityShulkerBox box;

    public CraftShulkerBox(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        box = (TileEntityShulkerBox) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftShulkerBox(final Material material, final TileEntityShulkerBox te) {
        super(material, te);

        box = te;
        world = null;
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(box);
    }
}
