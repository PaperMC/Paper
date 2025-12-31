package org.bukkit.entity;

import org.bukkit.inventory.ArmoredSaddledMountInventory;
import org.bukkit.inventory.InventoryHolder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AbstractNautilus extends Tameable, InventoryHolder, Vehicle {

    @Override
    ArmoredSaddledMountInventory getInventory();
}
