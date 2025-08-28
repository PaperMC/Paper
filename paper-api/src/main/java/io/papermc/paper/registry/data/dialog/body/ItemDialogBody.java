package io.papermc.paper.registry.data.dialog.body;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

/**
 * An item body for a dialog.
 * <p>Created via {@link DialogBody#item(ItemStack, PlainMessageDialogBody, boolean, boolean, int, int)}</p>
 */
@ApiStatus.NonExtendable
public non-sealed interface ItemDialogBody extends DialogBody {

    /**
     * The item to display in the dialog.
     *
     * @return the item stack
     */
    @Contract(pure = true)
    ItemStack item();

    /**
     * The description of the body, or null if not set.
     *
     * @return the description body
     */
    @Contract(pure = true)
    @Nullable PlainMessageDialogBody description();

    /**
     * Whether to show decorations around the item.
     * <p>Decorations include damage, itemstack count, etc.</p>
     *
     * @return true if decorations should be shown
     */
    @Contract(pure = true)
    boolean showDecorations();

    /**
     * Whether to show a tooltip for the item.
     *
     * @return true if a tooltip should be shown
     */
    @Contract(pure = true)
    boolean showTooltip();

    /**
     * The width of the item body.
     *
     * @return the width
     */
    @Contract(pure = true)
    @Range(from = 1, to = 256) int width();

    /**
     * The height of the item body.
     *
     * @return the height
     */
    @Contract(pure = true)
    @Range(from = 1, to = 256) int height();

    /**
     * A builder for an item dialog body.
     */
    @ApiStatus.NonExtendable
    interface Builder {

        /**
         * Sets the description of the item dialog body, or null if not set.
         *
         * @param description the description of the body, or null
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(@Nullable PlainMessageDialogBody description);

        /**
         * Sets whether to show decorations around the item.
         *
         * @param showDecorations true to show decorations, false otherwise
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder showDecorations(boolean showDecorations);

        /**
         * Sets whether to show a tooltip for the item.
         *
         * @param showTooltip true to show a tooltip, false otherwise
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder showTooltip(boolean showTooltip);

        /**
         * Sets the width of the item body.
         *
         * @param width the width, must be between 1 and 256
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder width(@Range(from = 1, to = 256) int width);

        /**
         * Sets the height of the item body.
         *
         * @param height the height, must be between 1 and 256
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder height(@Range(from = 1, to = 256) int height);

        /**
         * Builds a new instance of {@link ItemDialogBody}.
         *
         * @return a new item dialog body instance
         */
        @Contract(value = "-> new", pure = true)
        ItemDialogBody build();
    }
}
