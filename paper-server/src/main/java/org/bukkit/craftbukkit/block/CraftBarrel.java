package org.bukkit.craftbukkit.block;

import io.papermc.paper.block.LidMode;
import io.papermc.paper.block.LidState;
import io.papermc.paper.block.PaperLidded;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class CraftBarrel extends CraftLootable<BarrelBlockEntity> implements Barrel, PaperLidded {

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
    public CraftBarrel copy() {
        return new CraftBarrel(this, null);
    }

    @Override
    public CraftBarrel copy(Location location) {
        return new CraftBarrel(this, location);
    }

    @Override
    public void startForceLiddedLidOpen() {
        this.requirePlaced();
        this.getTileEntity().openersCounter.startForceLiddedLidOpen(this.getTileEntity().getLevel(), this.getTileEntity().getBlockPos(), this.getTileEntity().getBlockState());
    }

    @Override
    public void stopForceLiddedLidOpen() {
        this.requirePlaced();
        this.getTileEntity().openersCounter.stopForceLiddedLidOpen(this.getTileEntity().getLevel(), this.getTileEntity().getBlockPos(), this.getTileEntity().getBlockState());
    }

    @Override
    public void startForceLiddedLidClose() {
        this.requirePlaced();
        this.getTileEntity().openersCounter.startForceLiddedLidClose(this.getTileEntity().getLevel(), this.getTileEntity().getBlockPos(), this.getTileEntity().getBlockState());
    }

    @Override
    public void stopForceLiddedLidClose() {
        this.requirePlaced();
        this.getTileEntity().openersCounter.stopForceLiddedLidClose(this.getTileEntity().getLevel(), this.getTileEntity().getBlockPos(), this.getTileEntity().getBlockState());
    }

    @Override
    public @NotNull LidState getEffectiveLidState() {
        this.requirePlaced();
        return this.getTileEntity().openersCounter.getEffectiveLidState();
    }

    @Override
    public @NotNull LidState getTrueLidState() {
        this.requirePlaced();
        return this.getTileEntity().openersCounter.getTrueLidState();
    }

    @Override
    public @NotNull LidMode getLidMode() {
        this.requirePlaced();
        return this.getTileEntity().openersCounter.getLidMode();
    }

    @Override
    public @NotNull LidMode setLidMode(final @NotNull LidMode targetLidMode) {
        this.requirePlaced();
        LidMode newEffectiveMode = PaperLidded.super.setLidMode(targetLidMode);
        this.getTileEntity().openersCounter.setLidMode(newEffectiveMode);
        return newEffectiveMode;
    }

}
