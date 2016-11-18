package org.bukkit.craftbukkit.block;

import net.minecraft.server.BlockShulkerBox;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityShulkerBox;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftShulkerBox extends CraftLootable implements ShulkerBox {

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
    public TileEntity getTileEntity() {
        return box;
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(box);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) ((BlockShulkerBox) box.getBlock()).b.getColorIndex());
    }
}
