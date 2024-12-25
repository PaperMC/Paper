package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.EnderChest;
import org.jetbrains.annotations.NotNull;

public class CraftEnderChest extends CraftBlockEntityState<EnderChestBlockEntity> implements EnderChest, io.papermc.paper.block.PaperLidded {

    public CraftEnderChest(World world, EnderChestBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftEnderChest(CraftEnderChest state, Location location) {
        super(state, location);
    }

    @Override
    public CraftEnderChest copy() {
        return new CraftEnderChest(this, null);
    }

    @Override
    public CraftEnderChest copy(Location location) {
        return new CraftEnderChest(this, location);
    }

    // Paper start - More Chest Block API
    @Override
    public boolean isBlocked() {
        // Uses the same logic as EnderChestBlock's check for opening container
        final net.minecraft.core.BlockPos abovePos = this.getPosition().above();
        return this.isPlaced() && this.getWorldHandle().getBlockState(abovePos).isRedstoneConductor(this.getWorldHandle(), abovePos);
    }
    // Paper end - More Chest Block API

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
    public io.papermc.paper.block.@NotNull LidState getEffectiveLidState() {
        this.requirePlaced();
        return this.getTileEntity().openersCounter.getEffectiveLidState();
    }

    @Override
    public io.papermc.paper.block.@NotNull LidState getTrueLidState() {
        this.requirePlaced();
        return this.getTileEntity().openersCounter.getTrueLidState();
    }

    @Override
    public io.papermc.paper.block.@NotNull LidMode getLidMode() {
        this.requirePlaced();
        return this.getTileEntity().openersCounter.getLidMode();
    }

    @Override
    public io.papermc.paper.block.@NotNull LidMode setLidMode(final io.papermc.paper.block.@NotNull LidMode targetLidMode) {
        this.requirePlaced();
        io.papermc.paper.block.LidMode newEffectiveMode = io.papermc.paper.block.PaperLidded.super.setLidMode(targetLidMode);
        this.getTileEntity().openersCounter.setLidMode(newEffectiveMode);
        return newEffectiveMode;
    }
}
