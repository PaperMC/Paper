package org.bukkit.inventory.view.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.InventoryView;
import org.jspecify.annotations.Nullable;

public interface LocationInventorySupportingInventoryViewBuilder<V extends InventoryView> extends LocationInventoryViewBuilder<V>, InventorySupportingInventoryViewBuilder<V> {
    @Override
    LocationInventorySupportingInventoryViewBuilder<V> title(final @Nullable Component title);

    @Override
    LocationInventorySupportingInventoryViewBuilder<V> copy();
}
