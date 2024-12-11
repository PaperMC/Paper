package org.bukkit.craftbukkit.inventory.view;

import com.google.common.base.Preconditions;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.view.BrewingStandView;

public class CraftBrewingStandView extends CraftInventoryView<BrewingStandMenu, BrewerInventory> implements BrewingStandView {

    public CraftBrewingStandView(final HumanEntity player, final BrewerInventory viewing, final BrewingStandMenu container) {
        super(player, viewing, container);
    }

    @Override
    public int getFuelLevel() {
        return this.container.getFuel();
    }

    @Override
    public int getBrewingTicks() {
        return this.container.getBrewingTicks();
    }

    @Override
    public void setFuelLevel(final int fuelLevel) {
        Preconditions.checkArgument(fuelLevel > 0, "The given fuel level must be greater than 0");
        this.container.setData(BrewingStandBlockEntity.DATA_FUEL_USES, fuelLevel);
    }

    @Override
    public void setBrewingTicks(final int brewingTicks) {
        Preconditions.checkArgument(brewingTicks > 0, "The given brewing ticks must be greater than 0");
        this.container.setData(BrewingStandBlockEntity.DATA_BREW_TIME, brewingTicks);
    }
}
