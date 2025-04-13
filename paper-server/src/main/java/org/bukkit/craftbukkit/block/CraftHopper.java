package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

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
}
