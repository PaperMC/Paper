package com.destroystokyo.paper.event.inventory;

import org.bukkit.Warning;
import org.bukkit.inventory.GrindstoneInventory;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an item is put in a slot for grinding in a Grindstone
 *
 * @deprecated use {@link org.bukkit.event.inventory.PrepareGrindstoneEvent}
 */
@Deprecated(since = "1.16.1")
@Warning
@NullMarked
public interface PrepareGrindstoneEvent extends PrepareResultEvent {

    @Override
    GrindstoneInventory getInventory();
}
