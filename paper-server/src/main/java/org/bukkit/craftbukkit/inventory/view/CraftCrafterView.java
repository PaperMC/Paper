package org.bukkit.craftbukkit.inventory.view;

import com.google.common.base.Preconditions;
import net.minecraft.world.inventory.CrafterMenu;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.CrafterInventory;
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
    public void setSlotDisabled(final int slot, final boolean disabled) {
        Preconditions.checkArgument(slot >= 0 && slot < 9, "Invalid slot index %s for Crafter", slot);

        this.container.setSlotState(slot, !disabled);
    }
}
