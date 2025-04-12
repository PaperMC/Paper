package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Crafter;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftCrafter extends CraftLootable<CrafterBlockEntity> implements Crafter {

    public CraftCrafter(World world, CrafterBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftCrafter(CraftCrafter state, Location location) {
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
    public CraftCrafter copy() {
        return new CraftCrafter(this, null);
    }

    @Override
    public CraftCrafter copy(Location location) {
        return new CraftCrafter(this, location);
    }

    @Override
    public int getCraftingTicks() {
        return this.getSnapshot().craftingTicksRemaining;
    }

    @Override
    public void setCraftingTicks(int ticks) {
      this.getSnapshot().setCraftingTicksRemaining(ticks);
    }

    @Override
    public boolean isSlotDisabled(int slot) {
        Preconditions.checkArgument(slot >= 0 && slot < 9, "Invalid slot index %s for Crafter", slot);

        return this.getSnapshot().isSlotDisabled(slot);
    }

    @Override
    public void setSlotDisabled(int slot, boolean disabled) {
        Preconditions.checkArgument(slot >= 0 && slot < 9, "Invalid slot index %s for Crafter", slot);

        this.getSnapshot().setSlotState(slot, disabled);
    }

    @Override
    public boolean isTriggered() {
        return this.getSnapshot().isTriggered();
    }

    @Override
    public void setTriggered(boolean triggered) {
        this.getSnapshot().setTriggered(triggered);
    }
}
