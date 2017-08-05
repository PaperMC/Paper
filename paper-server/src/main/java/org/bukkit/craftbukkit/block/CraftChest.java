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

public class CraftChest extends CraftLootable<TileEntityChest> implements Chest {

    public CraftChest(final Block block) {
        super(block, TileEntityChest.class);
    }

    public CraftChest(final Material material, final TileEntityChest te) {
        super(material, te);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getBlockInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public Inventory getInventory() {
        CraftInventory inventory = (CraftInventory) this.getBlockInventory();
        if (!isPlaced()) {
            return inventory;
        }

        // The logic here is basically identical to the logic in BlockChest.interact
        int x = this.getX();
        int y = this.getY();
        int z = this.getZ();
        CraftWorld world = (CraftWorld) this.getWorld();

        int id;
        if (world.getBlockTypeIdAt(x, y, z) == Material.CHEST.getId()) {
            id = Material.CHEST.getId();
        } else if (world.getBlockTypeIdAt(x, y, z) == Material.TRAPPED_CHEST.getId()) {
            id = Material.TRAPPED_CHEST.getId();
        } else {
            throw new IllegalStateException("CraftChest is not a chest but is instead " + world.getBlockAt(x, y, z));
        }

        if (world.getBlockTypeIdAt(x - 1, y, z) == id) {
            CraftInventory left = new CraftInventory((TileEntityChest) world.getHandle().getTileEntity(new BlockPosition(x - 1, y, z)));
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
}
