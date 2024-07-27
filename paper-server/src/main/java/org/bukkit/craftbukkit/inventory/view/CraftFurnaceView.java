package org.bukkit.craftbukkit.inventory.view;

import net.minecraft.world.inventory.ContainerFurnace;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.view.FurnaceView;

public class CraftFurnaceView extends CraftInventoryView<ContainerFurnace> implements FurnaceView {

    public CraftFurnaceView(final HumanEntity player, final Inventory viewing, final ContainerFurnace container) {
        super(player, viewing, container);
    }

    @Override
    public float getCookTime() {
        return container.getBurnProgress();
    }

    @Override
    public float getBurnTime() {
        return container.getLitProgress();
    }

    @Override
    public boolean isBurning() {
        return container.isLit();
    }

    @Override
    public void setCookTime(final int cookProgress, final int cookDuration) {
        container.setData(TileEntityFurnace.DATA_COOKING_PROGRESS, cookProgress);
        container.setData(TileEntityFurnace.DATA_COOKING_TOTAL_TIME, cookDuration);
    }

    @Override
    public void setBurnTime(final int burnProgress, final int burnDuration) {
        container.setData(TileEntityFurnace.DATA_LIT_TIME, burnProgress);
        container.setData(TileEntityFurnace.DATA_LIT_DURATION, burnDuration);
    }
}
