package org.bukkit.inventory.view.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jspecify.annotations.Nullable;

public interface InventorySupportingInventoryViewBuilder<V extends InventoryView> extends InventoryViewBuilder<V> {
    InventorySupportingInventoryViewBuilder<V> inventory(Inventory inventory);

    @Override
    InventorySupportingInventoryViewBuilder<V> title(final @Nullable Component title);

    @Override
    InventorySupportingInventoryViewBuilder<V> copy();
}
