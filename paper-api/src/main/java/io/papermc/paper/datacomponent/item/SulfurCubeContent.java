package io.papermc.paper.datacomponent.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the item absorbed by a sulfur cube.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#SULFUR_CUBE_CONTENT
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface SulfurCubeContent {

    @Contract(value = "_ -> new", pure = true)
    static SulfurCubeContent sulfurCubeContent(final ItemStack absorbedItem) {
        return ItemComponentTypesBridge.bridge().sulfurCubeContent(absorbedItem);
    }

    /**
     * Gets the item absorbed by the cube.
     *
     * @return the item absorbed
     */
    @Contract(pure = true)
    ItemStack absorbedItem();
}
