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

    public CraftBarrel(World world, BarrelBlockEntity tileEntity) {
        super(world, tileEntity);
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

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public void open() {
        this.requirePlaced();
        if (!this.getTileEntity().openersCounter.opened) {
            BlockState blockData = this.getTileEntity().getBlockState();
            boolean open = blockData.getValue(BarrelBlock.OPEN);

            if (!open) {
                this.getTileEntity().updateBlockState(blockData, true);
                if (this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
                    this.getTileEntity().playSound(blockData, SoundEvents.BARREL_OPEN);
                }
            }
        }
        this.getTileEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        this.requirePlaced();
        if (this.getTileEntity().openersCounter.opened) {
            BlockState blockData = this.getTileEntity().getBlockState();
            this.getTileEntity().updateBlockState(blockData, false);
            if (this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
                this.getTileEntity().playSound(blockData, SoundEvents.BARREL_CLOSE);
            }
        }
        this.getTileEntity().openersCounter.opened = false;
    }

    @Override
    public CraftBarrel copy() {
        return new CraftBarrel(this, null);
    }

    @Override
    public CraftBarrel copy(Location location) {
        return new CraftBarrel(this, location);
    }
}
