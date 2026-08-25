package org.bukkit.event.inventory;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.inventory.SmithingInventory;

/**
 * Called when an item is put in a slot for upgrade by a Smithing Table.
 */
public interface PrepareSmithingEvent extends PrepareResultEvent {

    @Override
    SmithingInventory getInventory();
}
