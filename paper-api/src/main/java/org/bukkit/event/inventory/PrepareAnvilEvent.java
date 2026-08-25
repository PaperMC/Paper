package org.bukkit.event.inventory;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.AnvilView;
import org.jspecify.annotations.Nullable;

/**
 * Called when an item is put in a slot for repair by an anvil.
 */
public interface PrepareAnvilEvent extends PrepareResultEvent {

    @Override
    AnvilInventory getInventory();

    /**
     * {@inheritDoc}
     * <p>
     * Note: by default custom recipes in anvil are disabled
     * you should define a repair cost on the anvil inventory
     * greater or equals to zero in order to allow that.
     */
    @Override
    void setResult(@Nullable ItemStack result);

    @Override
    AnvilView getView();
}
