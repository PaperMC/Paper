package org.bukkit.craftbukkit.inventory.view;

import com.google.common.base.Preconditions;
import net.minecraft.world.inventory.CrafterMenu;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.CrafterInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.view.CrafterView;

public class CraftCrafterView extends CraftInventoryView<CrafterMenu, CrafterInventory> implements CrafterView {

    public CraftCrafterView(final HumanEntity player, final CrafterInventory viewing, final CrafterMenu container) {
        super(player, viewing, container);
    }

    @Override
    public boolean isSlotDisabled(final int slot) {
        return this.container.isSlotDisabled(slot);
    }

    @Override
    public boolean isPowered() {
        return this.container.isPowered();
    }

    @Override
    public int convertSlot(final int rawSlot) {
        // Crafter inventory size is 10, but the view uses non-contiguous raw slot IDs 0-8 (grid) and 45 (result).
        // The numInTop check and slot number shift lower in this method assume contiguous slot IDs.
        /*
         * Raw Slots:
         *
         *       0  1  2
         *       3  4  5     45
         *       6  7  8
         * 9  10 11 12 13 14 15 16 17
         * 18 19 20 21 22 23 24 25 26
         * 27 28 29 30 31 32 33 34 35
         * 36 37 38 39 40 41 42 43 44
         */

        /*
         * Converted Slots:
         *
         *       0  1  2
         *       3  4  5     9
         *       6  7  8
         * 9  10 11 12 13 14 15 16 17
         * 18 19 20 21 22 23 24 25 26
         * 27 28 29 30 31 32 33 34 35
         * 0  1  2  3  4  5  6  7  8
         */
        if (rawSlot == 45) {
            return 9; // Result
        } else if (rawSlot >= 36) {
            return rawSlot - 36; // Quickbar
        } else {
            return rawSlot; // Crafting grid or player inventory
        }
    }

    @Override
    public Inventory mapValidSlotToInventory(final int rawSlot) {
        // Raw slot ID for crafter inventory views is 45 for result slot and 0-8 for crafting grid slots.
        // Crafter inventory size is 10. Only check 0-8 and 45.
        if (rawSlot < (this.getTopInventory().getSize() - 1) || rawSlot == 45) {
            return this.getTopInventory();
        } else {
            return this.getBottomInventory();
        }
    }

    @Override
    public void setSlotDisabled(final int slot, final boolean disabled) {
        Preconditions.checkArgument(slot >= 0 && slot < 9, "Invalid slot index %s for Crafter", slot);

        this.container.setSlotState(slot, !disabled);
    }
}
