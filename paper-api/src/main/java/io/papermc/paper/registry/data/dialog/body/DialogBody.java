package io.papermc.paper.registry.data.dialog.body;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * Represents the body of a dialog.
 */
public sealed interface DialogBody permits DialogBody.ItemBody, DialogBody.PlainMessageBody {

    /**
     * Creates an item body for a dialog.
     *
     * @param item              the item to display in the dialog
     * @param description       the description of the body, or null if not set
     * @param showDecorations   whether to show decorations around the item
     * @param showTooltip       whether to show a tooltip for the item
     * @param width             the width of the item body
     * @param height            the height of the item body
     * @return a new item body instance
     */
    @Contract(pure = true, value = "_, _, _, _, _, _ -> new")
    static ItemBody item(
        final ItemStack item,
        final @Nullable PlainMessageBody description,
        final boolean showDecorations,
        final boolean showTooltip,
        final int width,
        final int height
    ) {
        return new ItemBodyImpl(item, description, showDecorations, showTooltip, width, height);
    }

    /**
     * Creates a plain message body for a dialog.
     *
     * @param contents the contents of the message
     * @param width    the width of the message body
     * @return a new plain message body instance
     */
    @Contract(pure = true, value = "_, _ -> new")
    static PlainMessageBody plainMessage(final Component contents, final int width) {
        return new PlainMessageBodyImpl(contents, width);
    }


    /**
     * An item body for a dialog.
     */
    sealed interface ItemBody extends DialogBody permits ItemBodyImpl {

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
        @Nullable
        PlainMessageBody description();

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
        int width();

        /**
         * The height of the item body.
         *
         * @return the height
         */
        @Contract(pure = true)
        int height();
    }

    /**
     * A plain message body for a dialog.
     */
    sealed interface PlainMessageBody extends DialogBody permits PlainMessageBodyImpl {

        /**
         * The contents of the plain message body.
         *
         * @return the component contents
         */
        @Contract(pure = true)
        Component contents();

        /**
         * The width of the plain message body.
         *
         * @return the width
         */
        @Contract(pure = true)
        int width();
    }
}
