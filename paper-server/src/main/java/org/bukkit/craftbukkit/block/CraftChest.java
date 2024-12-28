package org.bukkit.craftbukkit.block;

import io.papermc.paper.block.LidMode;
import io.papermc.paper.block.LidState;
import io.papermc.paper.block.PaperLidded;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class CraftChest extends CraftLootable<ChestBlockEntity> implements Chest, PaperLidded  {

    public CraftChest(World world, ChestBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftChest(CraftChest state, Location location) {
        super(state, location);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getBlockInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public Inventory getInventory() {
        CraftInventory inventory = (CraftInventory) this.getBlockInventory();
        if (!this.isPlaced() || this.isWorldGeneration()) {
            return inventory;
        }

        // The logic here is basically identical to the logic in BlockChest.interact
        CraftWorld world = (CraftWorld) this.getWorld();

        ChestBlock blockChest = (ChestBlock) (this.getType() == Material.CHEST ? Blocks.CHEST : Blocks.TRAPPED_CHEST);
        MenuProvider nms = blockChest.getMenuProvider(this.data, world.getHandle(), this.getPosition(), true);

        if (nms instanceof ChestBlock.DoubleInventory) {
            inventory = new CraftInventoryDoubleChest((ChestBlock.DoubleInventory) nms);
        }
        return inventory;
    }

    @Override
    public CraftChest copy() {
        return new CraftChest(this, null);
    }

    @Override
    public CraftChest copy(Location location) {
        return new CraftChest(this, location);
    }

    // Paper start - More Chest Block API
    @Override
    public boolean isBlocked() {
        // Method mimics vanilla logic in ChestBlock and DoubleBlockCombiner when trying to open chest's container
        if (!isPlaced()) {
            return false;
        }
        net.minecraft.world.level.LevelAccessor world = getWorldHandle();
        if (ChestBlock.isChestBlockedAt(world, getPosition())) {
            return true;
        }
        if (ChestBlock.getBlockType(this.data) == net.minecraft.world.level.block.DoubleBlockCombiner.BlockType.SINGLE) {
            return false;
        }
        net.minecraft.core.Direction direction = ChestBlock.getConnectedDirection(this.data);
        net.minecraft.core.BlockPos neighbourBlockPos = getPosition().relative(direction);
        BlockState neighbourBlockState = world.getBlockStateIfLoaded(neighbourBlockPos);
        return neighbourBlockState != null
            && neighbourBlockState.is(this.data.getBlock())
            && ChestBlock.getBlockType(neighbourBlockState) != net.minecraft.world.level.block.DoubleBlockCombiner.BlockType.SINGLE
            && ChestBlock.getConnectedDirection(neighbourBlockState) == direction.getOpposite()
            && ChestBlock.isChestBlockedAt(world, neighbourBlockPos);
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
