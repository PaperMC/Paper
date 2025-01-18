package org.bukkit.craftbukkit.block;

import io.papermc.paper.block.LidMode;
import io.papermc.paper.block.LidState;
import io.papermc.paper.block.PaperLidded;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class CraftShulkerBox extends CraftLootable<ShulkerBoxBlockEntity> implements ShulkerBox,
    PaperLidded {

    public CraftShulkerBox(World world, ShulkerBoxBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftShulkerBox(CraftShulkerBox state, Location location) {
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
    public DyeColor getColor() {
        net.minecraft.world.item.DyeColor color = ((ShulkerBoxBlock) CraftBlockType.bukkitToMinecraft(this.getType())).color;

        return (color == null) ? null : DyeColor.getByWoolData((byte) color.getId());
    }

    @Override
    public CraftShulkerBox copy() {
        return new CraftShulkerBox(this, null);
    }

    @Override
    public CraftShulkerBox copy(Location location) {
        return new CraftShulkerBox(this, location);
    }

    @Override
    public void startForceLiddedLidOpen() {
        this.requirePlaced();
        this.getTileEntity().startForceLiddedLidOpen();
    }

    @Override
    public void stopForceLiddedLidOpen() {
        this.requirePlaced();
        this.getTileEntity().stopForceLiddedLidOpen();
    }

    @Override
    public void startForceLiddedLidClose() {
        this.requirePlaced();
        this.getTileEntity().startForceLiddedLidClose();
    }

    @Override
    public void stopForceLiddedLidClose() {
        this.requirePlaced();
        this.getTileEntity().stopForceLiddedLidClose();
    }

    @Override
    public @NotNull LidState getEffectiveLidState() {
        this.requirePlaced();
        return this.getTileEntity().getEffectiveLidState();
    }

    @Override
    public @NotNull LidState getTrueLidState() {
        this.requirePlaced();
        return this.getTileEntity().getTrueLidState();
    }

    @Override
    public @NotNull LidMode getLidMode() {
        this.requirePlaced();
        return this.getTileEntity().getLidMode();
    }

    @Override
    public @NotNull LidMode setLidMode(final @NotNull LidMode targetLidMode) {
        this.requirePlaced();
        LidMode newEffectiveMode = PaperLidded.super.setLidMode(targetLidMode);
        this.getTileEntity().setLidMode(newEffectiveMode);
        return newEffectiveMode;
    }
}
