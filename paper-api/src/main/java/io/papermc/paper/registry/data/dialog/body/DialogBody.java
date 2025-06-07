package io.papermc.paper.registry.data.dialog.body;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * Represents the body of a dialog.
 */
public sealed interface DialogBody permits ItemDialogBody, PlainMessageDialogBody {

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
    static ItemDialogBody item(
        final ItemStack item,
        final @Nullable PlainMessageDialogBody description,
        final boolean showDecorations,
        final boolean showTooltip,
        final int width,
        final int height
    ) {
        return new ItemDialogBodyImpl(item, description, showDecorations, showTooltip, width, height);
    }

    /**
     * Creates a new item dialog body builder.
     *
     * @param item the item to display in the dialog
     * @return a new item dialog body builder instance
     */
    @Contract(pure = true, value = "_ -> new")
    static ItemDialogBody.Builder item(final ItemStack item) {
        return new ItemDialogBodyImpl.BuilderImpl(item);
    }

    /**
     * Creates a plain message body for a dialog.
     *
     * @param contents the contents of the message
     * @return a new plain message body instance
     */
    @Contract(pure = true, value = "_, -> new")
    static PlainMessageDialogBody plainMessage(final Component contents) {
        return new PlainMessageBodyImpl(contents, 200);
    }

    /**
     * Creates a plain message body for a dialog.
     *
     * @param contents the contents of the message
     * @param width    the width of the message body
     * @return a new plain message body instance
     */
    @Contract(pure = true, value = "_, _ -> new")
    static PlainMessageDialogBody plainMessage(final Component contents, final int width) {
        return new PlainMessageBodyImpl(contents, width);
    }
}
