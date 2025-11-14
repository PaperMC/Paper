package org.bukkit.entity;

import org.bukkit.inventory.ArmoredSaddledMountInventory;
import org.bukkit.inventory.InventoryHolder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AbstractNautilus extends Animals, InventoryHolder, Tameable {

    @Override
    ArmoredSaddledMountInventory getInventory();
}
