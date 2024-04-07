package io.papermc.paper.event.player;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CartographyInventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the recipe of an Item is completed inside a cartography table.
 */
@NullMarked
public class CartographyItemEvent extends InventoryClickEvent {

    @ApiStatus.Internal
    public CartographyItemEvent(final InventoryView view, final InventoryType.SlotType type, final int slot, final ClickType click, final InventoryAction action) {
        super(view, type, slot, click, action);
    }

    @ApiStatus.Internal
    public CartographyItemEvent(final InventoryView view, final InventoryType.SlotType type, final int slot, final ClickType click, final InventoryAction action, final int key) {
        super(view, type, slot, click, action, key);
    }

    @Override
    public CartographyInventory getInventory() {
        return (CartographyInventory) super.getInventory();
    }
}
