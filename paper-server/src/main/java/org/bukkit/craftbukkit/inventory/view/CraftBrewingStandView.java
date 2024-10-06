package org.bukkit.craftbukkit.inventory.view;

import com.google.common.base.Preconditions;
import net.minecraft.world.inventory.ContainerBrewingStand;
import net.minecraft.world.level.block.entity.TileEntityBrewingStand;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.view.BrewingStandView;

public class CraftBrewingStandView extends CraftInventoryView<ContainerBrewingStand, BrewerInventory> implements BrewingStandView {

    public CraftBrewingStandView(final HumanEntity player, final BrewerInventory viewing, final ContainerBrewingStand container) {
        super(player, viewing, container);
    }

    @Override
    public int getFuelLevel() {
        return container.getFuel();
    }

    @Override
    public int getBrewingTicks() {
        return container.getBrewingTicks();
    }

    @Override
    public void setFuelLevel(final int fuelLevel) {
        Preconditions.checkArgument(fuelLevel > 0, "The given fuel level must be greater than 0");
        container.setData(TileEntityBrewingStand.DATA_FUEL_USES, fuelLevel);
    }

    @Override
    public void setBrewingTicks(final int brewingTicks) {
        Preconditions.checkArgument(brewingTicks > 0, "The given brewing ticks must be greater than 0");
        container.setData(TileEntityBrewingStand.DATA_BREW_TIME, brewingTicks);
    }
}
