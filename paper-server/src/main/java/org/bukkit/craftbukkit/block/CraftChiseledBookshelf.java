package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.craftbukkit.inventory.CraftInventoryChiseledBookshelf;
import org.bukkit.inventory.ChiseledBookshelfInventory;
import org.bukkit.util.Vector;

public class CraftChiseledBookshelf extends CraftBlockEntityState<ChiseledBookShelfBlockEntity> implements ChiseledBookshelf, CraftSelectable<ChiseledBookShelfBlock> {

    public CraftChiseledBookshelf(World world, ChiseledBookShelfBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftChiseledBookshelf(CraftChiseledBookshelf state, Location location) {
        super(state, location);
    }

    @Override
    public int getLastInteractedSlot() {
        return this.getSnapshot().getLastInteractedSlot();
    }

    @Override
    public void setLastInteractedSlot(int lastInteractedSlot) {
        this.getSnapshot().lastInteractedSlot = lastInteractedSlot;
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

        return new CraftInventoryChiseledBookshelf(this.getBlockEntity());
    }

    @Override
    public int getSlot(Vector clickVector) {
        return CraftSelectable.super.getSlot(clickVector);
    }

    @Override
    public CraftChiseledBookshelf copy() {
        return new CraftChiseledBookshelf(this, null);
    }

    @Override
    public CraftChiseledBookshelf copy(Location location) {
        return new CraftChiseledBookshelf(this, location);
    }
}
