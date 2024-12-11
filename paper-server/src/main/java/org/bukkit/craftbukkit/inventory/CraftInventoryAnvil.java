package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import net.minecraft.world.Container;
import org.bukkit.Location;
import org.bukkit.craftbukkit.inventory.view.CraftAnvilView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.AnvilInventory;

public class CraftInventoryAnvil extends CraftResultInventory implements AnvilInventory {

    private static final int DEFAULT_REPAIR_COST = 0;
    private static final int DEFAULT_REPAIR_COST_AMOUNT = 0;
    private static final int DEFAULT_MAXIMUM_REPAIR_COST = 40;

    private final Location location;
    private String renameText;
    private int repairCost;
    private int repairCostAmount;
    private int maximumRepairCost;

    public CraftInventoryAnvil(Location location, Container inventory, Container resultInventory) {
        super(inventory, resultInventory);
        this.location = location;
        this.renameText = null;
        this.repairCost = CraftInventoryAnvil.DEFAULT_REPAIR_COST;
        this.repairCostAmount = CraftInventoryAnvil.DEFAULT_REPAIR_COST_AMOUNT;
        this.maximumRepairCost = CraftInventoryAnvil.DEFAULT_MAXIMUM_REPAIR_COST;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public String getRenameText() {
        this.syncWithArbitraryViewValue((cav) -> this.renameText = cav.getRenameText());
        return this.renameText;
    }

    @Override
    public int getRepairCostAmount() {
        this.syncWithArbitraryViewValue((cav) -> this.repairCostAmount = cav.getRepairItemCountCost());
        return this.repairCostAmount;
    }

    @Override
    public void setRepairCostAmount(int amount) {
        this.repairCostAmount = amount;
        this.syncViews((cav) -> cav.setRepairItemCountCost(amount));
    }

    @Override
    public int getRepairCost() {
        this.syncWithArbitraryViewValue((cav) -> this.repairCost = cav.getRepairCost());
        return this.repairCost;
    }

    @Override
    public void setRepairCost(int i) {
        this.repairCost = i;
        this.syncViews((cav) -> cav.setRepairCost(i));
    }

    @Override
    public int getMaximumRepairCost() {
        this.syncWithArbitraryViewValue((cav) -> this.maximumRepairCost = cav.getMaximumRepairCost());
        return this.maximumRepairCost;
    }

    @Override
    public void setMaximumRepairCost(int levels) {
        Preconditions.checkArgument(levels >= 0, "Maximum repair cost must be positive (or 0)");
        this.maximumRepairCost = levels;
        this.syncViews((cav) -> cav.setMaximumRepairCost(levels));
    }

    public boolean isRepairCostSet() {
        return this.repairCost != CraftInventoryAnvil.DEFAULT_REPAIR_COST;
    }

    public boolean isRepairCostAmountSet() {
        return this.repairCostAmount != CraftInventoryAnvil.DEFAULT_REPAIR_COST_AMOUNT;
    }

    public boolean isMaximumRepairCostSet() {
        return this.maximumRepairCost != CraftInventoryAnvil.DEFAULT_MAXIMUM_REPAIR_COST;
    }

    // used to lazily update and apply values from the view to the inventory
    private void syncViews(Consumer<CraftAnvilView> consumer) {
        for (HumanEntity viewer : this.getViewers()) {
            if (viewer.getOpenInventory() instanceof CraftAnvilView cav) {
                consumer.accept(cav);
            }
        }
    }

    /*
     * This method provides the best effort guess on whatever the value could be
     * It is possible these values are wrong given there are more than 1 views of this inventory,
     * however it is a limitation seeing as these anvil values are supposed to be in the Container
     * not the inventory.
     */
    private void syncWithArbitraryViewValue(Consumer<CraftAnvilView> consumer) {
        if (this.getViewers().isEmpty()) {
            return;
        }
        final HumanEntity entity = this.getViewers().get(0);
        if (entity != null && entity.getOpenInventory() instanceof CraftAnvilView cav) {
            consumer.accept(cav);
        }
    }
}
