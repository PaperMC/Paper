package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import org.bukkit.World;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.craftbukkit.inventory.CraftInventoryChiseledBookshelf;
import org.bukkit.inventory.ChiseledBookshelfInventory;

public class CraftChiseledBookshelf extends CraftBlockEntityState<ChiseledBookShelfBlockEntity> implements ChiseledBookshelf {

    public CraftChiseledBookshelf(World world, ChiseledBookShelfBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public int getLastInteractedSlot() {
        return getSnapshot().getLastInteractedSlot();
    }

    @Override
    public void setLastInteractedSlot(int lastInteractedSlot) {
        getSnapshot().lastInteractedSlot = lastInteractedSlot;
    }

    @Override
    public ChiseledBookshelfInventory getSnapshotInventory() {
        return new CraftInventoryChiseledBookshelf(this.getSnapshot());
    }

    @Override
    public ChiseledBookshelfInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryChiseledBookshelf(this.getTileEntity());
    }
}
