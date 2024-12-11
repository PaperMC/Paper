package org.bukkit.craftbukkit.inventory.view;

import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.view.FurnaceView;

public class CraftFurnaceView extends CraftInventoryView<AbstractFurnaceMenu, FurnaceInventory> implements FurnaceView {

    public CraftFurnaceView(final HumanEntity player, final FurnaceInventory viewing, final AbstractFurnaceMenu container) {
        super(player, viewing, container);
    }

    @Override
    public float getCookTime() {
        return this.container.getBurnProgress();
    }

    @Override
    public float getBurnTime() {
        return this.container.getLitProgress();
    }

    @Override
    public boolean isBurning() {
        return this.container.isLit();
    }

    @Override
    public void setCookTime(final int cookProgress, final int cookDuration) {
        this.container.setData(AbstractFurnaceBlockEntity.DATA_COOKING_PROGRESS, cookProgress);
        this.container.setData(AbstractFurnaceBlockEntity.DATA_COOKING_TOTAL_TIME, cookDuration);
    }

    @Override
    public void setBurnTime(final int burnProgress, final int burnDuration) {
        this.container.setData(AbstractFurnaceBlockEntity.DATA_LIT_TIME, burnProgress);
        this.container.setData(AbstractFurnaceBlockEntity.DATA_LIT_DURATION, burnDuration);
    }
}
