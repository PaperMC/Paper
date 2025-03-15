package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftDropper extends CraftLootable<DropperBlockEntity> implements Dropper {

    public CraftDropper(World world, DropperBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftDropper(CraftDropper state, Location location) {
        super(state, location);
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

        return new CraftInventory(this.getBlockEntity());
    }

    @Override
    public void drop() {
        this.ensureNoWorldGeneration();
        Block block = this.getBlock();
        if (block.getType() == Material.DROPPER) {
            CraftWorld world = (CraftWorld) this.getWorld();
            DropperBlock drop = (DropperBlock) Blocks.DROPPER;

            drop.dispenseFrom(world.getHandle(), this.getHandle(), this.getPosition());
        }
    }

    @Override
    public CraftDropper copy() {
        return new CraftDropper(this, null);
    }

    @Override
    public CraftDropper copy(Location location) {
        return new CraftDropper(this, location);
    }
}
