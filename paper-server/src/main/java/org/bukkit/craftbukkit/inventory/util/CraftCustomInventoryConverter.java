package org.bukkit.craftbukkit.inventory.util;

import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftCustomInventoryConverter implements CraftInventoryCreator.InventoryConverter {

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return new CraftInventoryCustom(holder, type);
    }

    // Paper start
    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, net.kyori.adventure.text.Component title) {
        return new CraftInventoryCustom(owner, type, title);
    }
    // Paper end

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return new CraftInventoryCustom(owner, type, title);
    }

    public Inventory createInventory(InventoryHolder owner, int size) {
        return new CraftInventoryCustom(owner, size);
    }

    // Paper start
    public Inventory createInventory(InventoryHolder owner, int size, net.kyori.adventure.text.Component title) {
        return new CraftInventoryCustom(owner, size, title);
    }
    // Paper end

    public Inventory createInventory(InventoryHolder owner, int size, String title) {
        return new CraftInventoryCustom(owner, size, title);
    }
}
