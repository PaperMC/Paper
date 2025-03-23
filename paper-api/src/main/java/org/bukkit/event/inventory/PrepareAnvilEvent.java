package org.bukkit.event.inventory;

import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.AnvilView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an item is put in a slot for repair by an anvil.
 */
public class PrepareAnvilEvent extends com.destroystokyo.paper.event.inventory.PrepareResultEvent {

    @ApiStatus.Internal
    public PrepareAnvilEvent(@NotNull AnvilView inventory, @Nullable ItemStack result) {
        super(inventory, result);
    }

    @NotNull
    @Override
    public AnvilInventory getInventory() {
        return (AnvilInventory) super.getInventory();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note: by default custom recipes in anvil are disabled
     * you should define a repair cost on the anvil inventory
     * greater or equals to zero in order to allow that.
     */
    public void setResult(@Nullable ItemStack result) {
        super.setResult(result);
    }

    @NotNull
    @Override
    public AnvilView getView() {
        return (AnvilView) super.getView();
    }
}
