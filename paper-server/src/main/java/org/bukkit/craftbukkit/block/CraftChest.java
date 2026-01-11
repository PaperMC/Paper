package org.bukkit.craftbukkit.block;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;

public class CraftChest extends CraftLootable<ChestBlockEntity> implements Chest {

    public CraftChest(World world, ChestBlockEntity blockEntity) {
        super(world, blockEntity);
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

        return new CraftInventory(this.getBlockEntity());
    }

    @Override
    public Inventory getInventory() {
        CraftInventory inventory = (CraftInventory) this.getBlockInventory();
        if (!this.isPlaced() || this.isWorldGeneration()) {
            return inventory;
        }

        CraftWorld world = (CraftWorld) this.getWorld();

        ChestBlock block = this.data.getBlock() instanceof ChestBlock chestBlock ? chestBlock : (ChestBlock) Blocks.CHEST;
        MenuProvider provider = block.getMenuProvider(this.data, world.getHandle(), this.getPosition(), true);

        if (provider instanceof ChestBlock.DoubleInventory) {
            inventory = new CraftInventoryDoubleChest((ChestBlock.DoubleInventory) provider);
        }
        return inventory;
    }

    @Override
    public void open() {
        this.requirePlaced();
        if (!this.getBlockEntity().openersCounter.opened && this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
            BlockState block = this.getBlockEntity().getBlockState();
            int openCount = this.getBlockEntity().openersCounter.getOpenerCount();

            this.getBlockEntity().openersCounter.onAPIOpen((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block);
            this.getBlockEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block, openCount, openCount + 1);
        }
        this.getBlockEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        this.requirePlaced();
        if (this.getBlockEntity().openersCounter.opened && this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
            BlockState block = this.getBlockEntity().getBlockState();
            int openCount = this.getBlockEntity().openersCounter.getOpenerCount();

            this.getBlockEntity().openersCounter.onAPIClose((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block);
            this.getBlockEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block, openCount, 0);
        }
        this.getBlockEntity().openersCounter.opened = false;
    }

    @Override
    public CraftChest copy() {
        return new CraftChest(this, null);
    }

    @Override
    public CraftChest copy(Location location) {
        return new CraftChest(this, location);
    }

    // Paper start - More Lidded Block API
    @Override
    public boolean isOpen() {
        return getBlockEntity().openersCounter.opened;
    }
    // Paper end - More Lidded Block API

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
}
