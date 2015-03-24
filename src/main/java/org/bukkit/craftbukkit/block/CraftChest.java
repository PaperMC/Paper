package org.bukkit.craftbukkit.block;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.TileEntityChest;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;

public class CraftChest extends CraftBlockState implements Chest {
    private final CraftWorld world;
    private final TileEntityChest chest;

    public CraftChest(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        chest = (TileEntityChest) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftChest(final Material material, final TileEntityChest te) {
        super(material);
        chest = te;
        world = null;
    }

    public Inventory getBlockInventory() {
        return new CraftInventory(chest);
    }

    public Inventory getInventory() {
        int x = getX();
        int y = getY();
        int z = getZ();
        // The logic here is basically identical to the logic in BlockChest.interact
        CraftInventory inventory = new CraftInventory(chest);
        if (!isPlaced()) {
            return inventory;
        }
        int id;
        if (world.getBlockTypeIdAt(x, y, z) == Material.CHEST.getId()) {
            id = Material.CHEST.getId();
        } else if (world.getBlockTypeIdAt(x, y, z) == Material.TRAPPED_CHEST.getId()) {
            id = Material.TRAPPED_CHEST.getId();
        } else {
            throw new IllegalStateException("CraftChest is not a chest but is instead " + world.getBlockAt(x, y, z));
        }

        if (world.getBlockTypeIdAt(x - 1, y, z) == id) {
            CraftInventory left = new CraftInventory((TileEntityChest)world.getHandle().getTileEntity(new BlockPosition(x - 1, y, z)));
            inventory = new CraftInventoryDoubleChest(left, inventory);
        }
        if (world.getBlockTypeIdAt(x + 1, y, z) == id) {
            CraftInventory right = new CraftInventory((TileEntityChest) world.getHandle().getTileEntity(new BlockPosition(x + 1, y, z)));
            inventory = new CraftInventoryDoubleChest(inventory, right);
        }
        if (world.getBlockTypeIdAt(x, y, z - 1) == id) {
            CraftInventory left = new CraftInventory((TileEntityChest) world.getHandle().getTileEntity(new BlockPosition(x, y, z - 1)));
            inventory = new CraftInventoryDoubleChest(left, inventory);
        }
        if (world.getBlockTypeIdAt(x, y, z + 1) == id) {
            CraftInventory right = new CraftInventory((TileEntityChest) world.getHandle().getTileEntity(new BlockPosition(x, y, z + 1)));
            inventory = new CraftInventoryDoubleChest(inventory, right);
        }
        return inventory;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            chest.update();
        }

        return result;
    }

    @Override
    public TileEntityChest getTileEntity() {
        return chest;
    }
}
