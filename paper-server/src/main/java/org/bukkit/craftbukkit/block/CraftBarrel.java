package org.bukkit.craftbukkit.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftBarrel extends CraftLootable<BarrelBlockEntity> implements Barrel {

    public CraftBarrel(World world, BarrelBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftBarrel(CraftBarrel state, Location location) {
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
    public void open() {
        this.requirePlaced();
        if (!this.getBlockEntity().openersCounter.opened) {
            BlockState state = this.getBlockEntity().getBlockState();
            boolean open = state.getValue(BarrelBlock.OPEN);

            if (!open) {
                this.getBlockEntity().updateBlockState(state, true);
                if (this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
                    this.getBlockEntity().playSound(state, SoundEvents.BARREL_OPEN);
                }
            }
        }
        this.getBlockEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        this.requirePlaced();
        if (this.getBlockEntity().openersCounter.opened) {
            BlockState state = this.getBlockEntity().getBlockState();
            this.getBlockEntity().updateBlockState(state, false);
            if (this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
                this.getBlockEntity().playSound(state, SoundEvents.BARREL_CLOSE);
            }
        }
        this.getBlockEntity().openersCounter.opened = false;
    }

    @Override
    public CraftBarrel copy() {
        return new CraftBarrel(this, null);
    }

    @Override
    public CraftBarrel copy(Location location) {
        return new CraftBarrel(this, location);
    }

    // Paper start - More Lidded Block API
    @Override
    public boolean isOpen() {
        return getBlockEntity().openersCounter.opened;
    }
    // Paper end - More Lidded Block API
}
