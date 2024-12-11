package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.EnderChest;

public class CraftEnderChest extends CraftBlockEntityState<EnderChestBlockEntity> implements EnderChest {

    public CraftEnderChest(World world, EnderChestBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftEnderChest(CraftEnderChest state, Location location) {
        super(state, location);
    }

    @Override
    public void open() {
        this.requirePlaced();
        if (!this.getTileEntity().openersCounter.opened && this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
            BlockState block = this.getTileEntity().getBlockState();
            int openCount = this.getTileEntity().openersCounter.getOpenerCount();

            this.getTileEntity().openersCounter.onAPIOpen((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block);
            this.getTileEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block, openCount, openCount + 1);
        }
        this.getTileEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        this.requirePlaced();
        if (this.getTileEntity().openersCounter.opened && this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
            BlockState block = this.getTileEntity().getBlockState();
            int openCount = this.getTileEntity().openersCounter.getOpenerCount();

            this.getTileEntity().openersCounter.onAPIClose((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block);
            this.getTileEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block, openCount, 0);
        }
        this.getTileEntity().openersCounter.opened = false;
    }

    @Override
    public CraftEnderChest copy() {
        return new CraftEnderChest(this, null);
    }

    @Override
    public CraftEnderChest copy(Location location) {
        return new CraftEnderChest(this, location);
    }
}
