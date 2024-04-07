package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Crafter;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftCrafter extends CraftLootable<CrafterBlockEntity> implements Crafter {

    public CraftCrafter(World world, CrafterBlockEntity tileEntity) {
        super(world, tileEntity);
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

        return new CraftInventory(this.getTileEntity());
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
        return getSnapshot().craftingTicksRemaining;
    }

    @Override
    public void setCraftingTicks(int ticks) {
      getSnapshot().setCraftingTicksRemaining(ticks);
    }

    @Override
    public boolean isSlotDisabled(int slot) {
        Preconditions.checkArgument(slot >= 0 && slot < 9, "Invalid slot index %s for Crafter", slot);

        return getSnapshot().isSlotDisabled(slot);
    }

    @Override
    public void setSlotDisabled(int slot, boolean disabled) {
        Preconditions.checkArgument(slot >= 0 && slot < 9, "Invalid slot index %s for Crafter", slot);

        getSnapshot().setSlotState(slot, disabled);
    }

    @Override
    public boolean isTriggered() {
        return getSnapshot().isTriggered();
    }

    @Override
    public void setTriggered(boolean triggered) {
        getSnapshot().setTriggered(triggered);
    }
}
