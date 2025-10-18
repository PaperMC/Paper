package io.papermc.paper.inventory;

import java.util.Collection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a tab in the creative inventory.
 *
 * @see CreativeModeTabs
 */
@NullMarked
public interface CreativeModeTab extends Keyed, Translatable {

    /**
     * {@return the user-friendly display name for this tab}
     */
    Component displayName();

    /**
     * {@return the item that is used as the icon in the creative inventory}
     */
    ItemStack iconItem();

    /**
     * @return {@code true} if the scrollbar is visible on the right while this tab is focused
     */
    boolean scrollbarShown();

    /**
     * @param type the item type
     * @return {@code true} if this tab contains an item with the specified item type
     */
    boolean containsItem(ItemType type);

    /**
     * @param stack the item stack
     * @return {@code true} if this tab contains the specified item
     */
    boolean containsItemStack(ItemStack stack);

    /**
     * {@return an unmodifiable collection of items that are included in this tab}
     */
    @Unmodifiable Collection<ItemStack> getContents();

    /**
     * {@return the row that this tab is in}
     */
    Row getRow();

    /**
     * {@return the tab's type}
     */
    Type getType();

    enum Row {
        // Start generate - CreativeModeTabRow
        TOP,
        BOTTOM;
        // End generate - CreativeModeTabRow
    }

    enum Type {
        // Start generate - CreativeModeTabType
        CATEGORY,
        INVENTORY,
        HOTBAR,
        SEARCH;
        // End generate - CreativeModeTabType
    }
}
