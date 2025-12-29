package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Shelf;
import org.bukkit.craftbukkit.inventory.CraftInventoryShelf;
import org.bukkit.inventory.ShelfInventory;
import org.bukkit.util.Vector;

public class CraftShelf extends CraftBlockEntityState<ShelfBlockEntity> implements Shelf, CraftSelectable<ShelfBlock> {

    public CraftShelf(World world, ShelfBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftShelf(CraftShelf state, Location location) {
        super(state, location);
    }

    @Override
    public ShelfInventory getSnapshotInventory() {
        return new CraftInventoryShelf(this.getSnapshot());
    }

    @Override
    public ShelfInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryShelf(this.getBlockEntity());
    }

    @Override
    public int getSlot(Vector clickVector) {
        return CraftSelectable.super.getSlot(clickVector);
    }

    @Override
    public CraftBlockEntityState<ShelfBlockEntity> copy() {
        return new CraftShelf(this, null);
    }

    @Override
    public CraftBlockEntityState<ShelfBlockEntity> copy(final Location location) {
        return new CraftShelf(this, location);
    }
}
