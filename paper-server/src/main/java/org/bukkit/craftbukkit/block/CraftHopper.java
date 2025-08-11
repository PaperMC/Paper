package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public class CraftHopper extends CraftLootable<HopperBlockEntity> implements Hopper {

    public CraftHopper(World world, HopperBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftHopper(CraftHopper state, Location location) {
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
    public CraftHopper copy() {
        return new CraftHopper(this, null);
    }

    @Override
    public CraftHopper copy(Location location) {
        return new CraftHopper(this, location);
    }

    // Paper start - Expanded Hopper API
    @Override
    public void setTransferCooldown(final int cooldown) {
        com.google.common.base.Preconditions.checkArgument(cooldown >= 0, "Hooper transfer cooldown cannot be negative (" + cooldown + ")");
        getSnapshot().setCooldown(cooldown);
    }

    @Override
    public int getTransferCooldown() {
        return getSnapshot().cooldownTime;
    }
    // Paper end - Expanded Hopper API

    // Paper start - Allow you to set the number of items that a hopper moves
    @Override
    public void setTransferAmount(@Nullable final Integer transferAmount) {
        if(transferAmount != null){
            com.google.common.base.Preconditions.checkArgument(transferAmount > 0, "Hopper transfer amount cannot be less than 1");
        }
        this.getSnapshot().setTransferAmount(transferAmount);
    }

    @Override
    public Optional<Integer> getTransferAmount() {
        return this.getSnapshot().getTransferAmount();
    }
    //Paper end - Allow you to set the number of items that a hopper moves
}
