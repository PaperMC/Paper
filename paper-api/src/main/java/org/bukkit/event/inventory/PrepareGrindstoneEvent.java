package org.bukkit.event.inventory;

import org.bukkit.inventory.GrindstoneInventory;

/**
 * Called when an item is put in a slot for repair or unenchanting in a grindstone.
 */
public interface PrepareGrindstoneEvent extends com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent { // Paper

    @Override
    GrindstoneInventory getInventory();
}
