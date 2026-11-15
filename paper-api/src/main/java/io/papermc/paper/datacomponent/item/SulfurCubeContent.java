package io.papermc.paper.datacomponent.item;

import io.papermc.paper.item.ItemStackTemplate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Holds the item absorbed by a sulfur cube.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#SULFUR_CUBE_CONTENT
 */
@ApiStatus.NonExtendable
public interface SulfurCubeContent {

    @Contract(value = "_ -> new", pure = true)
    static SulfurCubeContent sulfurCubeContent(final ItemStack absorbedItem) {
        return sulfurCubeContent(absorbedItem.toTemplate());
    }

    @Contract(value = "_ -> new", pure = true)
    static SulfurCubeContent sulfurCubeContent(final ItemStackTemplate absorbedItem) {
        return ItemComponentTypesBridge.bridge().sulfurCubeContent(absorbedItem);
    }

    /**
     * Gets the item absorbed by the cube.
     *
     * @return the item absorbed
     * @see #absorbedItemTemplate()
     */
    @Contract(pure = true)
    ItemStack absorbedItem();

    /**
     * Gets the item absorbed by the cube.
     * <p>
     * Unlike {@link #absorbedItem()}, this is available while registries are
     * still being loaded, such as in registry events.
     *
     * @return the item absorbed
     */
    @Contract(pure = true)
    ItemStackTemplate absorbedItemTemplate();
}
