package io.papermc.paper.datacomponent.item;

import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Holds if this item is repairable, and what item types it can be repaired with.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#REPAIRABLE
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface Repairable {

    @Contract(value = "_ -> new", pure = true)
    static Repairable repairable(final RegistryKeySet<ItemType> types) {
        return ItemComponentTypesBridge.bridge().repairable(types);
    }

    /**
     * The types that this item is repairable to.
     *
     * @return item
     */
    @Contract(value = "-> new", pure = true)
    RegistryKeySet<ItemType> types();
}
