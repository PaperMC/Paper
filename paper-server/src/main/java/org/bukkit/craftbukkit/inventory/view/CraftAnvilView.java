package org.bukkit.craftbukkit.inventory.view;

import net.minecraft.world.inventory.ContainerAnvil;
import org.bukkit.craftbukkit.inventory.CraftInventoryAnvil;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.view.AnvilView;
import org.jetbrains.annotations.Nullable;

public class CraftAnvilView extends CraftInventoryView<ContainerAnvil> implements AnvilView {

    public CraftAnvilView(final HumanEntity player, final Inventory viewing, final ContainerAnvil container) {
        super(player, viewing, container);
    }

    @Nullable
    @Override
    public String getRenameText() {
        return container.itemName;
    }

    @Override
    public int getRepairItemCountCost() {
        return container.repairItemCountCost;
    }

    @Override
    public int getRepairCost() {
        return container.getCost();
    }

    @Override
    public int getMaximumRepairCost() {
        return container.maximumRepairCost;
    }

    @Override
    public void setRepairItemCountCost(final int cost) {
        container.repairItemCountCost = cost;
    }

    @Override
    public void setRepairCost(final int cost) {
        container.cost.set(cost);
    }

    @Override
    public void setMaximumRepairCost(final int cost) {
        container.maximumRepairCost = cost;
    }

    public void updateFromLegacy(CraftInventoryAnvil legacy) {
        if (legacy.isRepairCostSet()) {
            setRepairCost(legacy.getRepairCost());
        }

        if (legacy.isRepairCostAmountSet()) {
            setRepairItemCountCost(legacy.getRepairCostAmount());
        }

        if (legacy.isMaximumRepairCostSet()) {
            setMaximumRepairCost(legacy.getMaximumRepairCost());
        }
    }
}
