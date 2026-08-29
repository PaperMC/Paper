package io.papermc.paper.inventory;

import java.util.Collection;
import net.kyori.adventure.text.Component;
import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a tab in the creative inventory GUI.
 *
 * @see CreativeModeTabs
 */
@NullMarked
public interface CreativeModeTab extends Keyed {

    /**
     * {@return the user-friendly display name for this tab}
     */
    Component displayName();

    /**
     * {@return the item that is used as the icon in the creative inventory GUI}
     */
    ItemStack iconItem();

    /**
     * {@return the position where the tab icon is located in the creative inventory GUI}
     */
    Position iconPosition();

    /**
     * @param item the item type
     * @return {@code true} if this tab contains an item with the specified item type
     */
    boolean containsItem(ItemType item);

    /**
     * @param item the item
     * @return {@code true} if this tab contains the specified item
     */
    boolean containsItem(ItemStack item);

    /**
     * {@return an unmodifiable collection of items that are included in this tab}
     */
    @Unmodifiable Collection<ItemStack> getContents();

    /**
     * {@return the tab's type}
     */
    Type getType();

    /**
     * The type of the tab.
     * <p>
     * Creative categories split items by common similarities while
     * the search tab hold all the items in those categories.
     * The hotbar saves and survival inventory belongs to the player and
     * are always considered empty by the server.
     */
    enum Type {
        // Start generate - CreativeModeTabType
        CATEGORY,
        INVENTORY,
        HOTBAR,
        SEARCH;
        // End generate - CreativeModeTabType
    }

    /**
     * The position of the tab icon in the creative inventory GUI.
     */
    interface Position {

        /**
         * {@return the row where the tab icon is located in, 0-indexed from top}
         */
        int row();

        /**
         * {@return the column where the tab icon is located in, 0-indexed from left}
         */
        int column();

        /**
         * {@return whether the tab icon is aligned at right}
         */
        boolean anchoredAtRight();
    }
}
