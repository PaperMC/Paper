package org.bukkit.craftbukkit.inventory.view;

import net.minecraft.world.inventory.AnvilMenu;
import org.bukkit.craftbukkit.inventory.CraftInventoryAnvil;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.view.AnvilView;
import org.jetbrains.annotations.Nullable;

public class CraftAnvilView extends CraftInventoryView<AnvilMenu, AnvilInventory> implements AnvilView {

    public CraftAnvilView(final HumanEntity player, final AnvilInventory viewing, final AnvilMenu container) {
        super(player, viewing, container);
    }

    @Nullable
    @Override
    public String getRenameText() {
        return this.container.itemName;
    }

    @Override
    public int getRepairItemCountCost() {
        return this.container.repairItemCountCost;
    }

    @Override
    public int getRepairCost() {
        return this.container.getCost();
    }

    @Override
    public int getMaximumRepairCost() {
        return this.container.maximumRepairCost;
    }

    @Override
    public void setRepairItemCountCost(final int cost) {
        this.container.repairItemCountCost = cost;
    }

    @Override
    public void setRepairCost(final int cost) {
        this.container.cost.set(cost);
    }

    @Override
    public void setMaximumRepairCost(final int cost) {
        this.container.maximumRepairCost = cost;
    }

    public void updateFromLegacy(CraftInventoryAnvil legacy) {
        if (legacy.isRepairCostSet()) {
            this.setRepairCost(legacy.getRepairCost());
        }

        if (legacy.isRepairCostAmountSet()) {
            this.setRepairItemCountCost(legacy.getRepairCostAmount());
        }

        if (legacy.isMaximumRepairCostSet()) {
            this.setMaximumRepairCost(legacy.getMaximumRepairCost());
        }
    }
}
