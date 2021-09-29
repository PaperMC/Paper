package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.BlockDropper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntityDropper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftDropper extends CraftLootable<TileEntityDropper> implements Dropper {

    public CraftDropper(World world, TileEntityDropper tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public void drop() {
        Preconditions.checkState(getWorldHandle() instanceof net.minecraft.world.level.World, "Can't drop during world generation");

        Block block = getBlock();

        if (block.getType() == Material.DROPPER) {
            CraftWorld world = (CraftWorld) this.getWorld();
            BlockDropper drop = (BlockDropper) Blocks.DROPPER;

            drop.dispense(world.getHandle(), this.getPosition());
        }
    }
}
