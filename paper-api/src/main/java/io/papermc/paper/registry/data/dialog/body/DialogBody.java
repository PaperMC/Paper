package io.papermc.paper.registry.data.dialog.body;

import io.papermc.paper.registry.data.dialog.DialogInstancesProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
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
        final @Range(from = 1, to = 256) int width,
        final @Range(from = 1, to = 256) int height
    ) {
        return item(item)
            .description(description)
            .showDecorations(showDecorations)
            .showTooltip(showTooltip)
            .width(width)
            .height(height)
            .build();
    }

    /**
     * Creates a new item dialog body builder.
     *
     * @param item the item to display in the dialog
     * @return a new item dialog body builder instance
     */
    @Contract(pure = true, value = "_ -> new")
    static ItemDialogBody.Builder item(final ItemStack item) {
        return DialogInstancesProvider.instance().itemDialogBodyBuilder(item);
    }

    /**
     * Creates a plain message body for a dialog.
     *
     * @param contents the contents of the message
     * @return a new plain message body instance
     */
    @Contract(pure = true, value = "_ -> new")
    static PlainMessageDialogBody plainMessage(final Component contents) {
        return DialogInstancesProvider.instance().plainMessageDialogBody(contents);
    }

    /**
     * Creates a plain message body for a dialog.
     *
     * @param contents the contents of the message
     * @param width    the width of the message body
     * @return a new plain message body instance
     */
    @Contract(pure = true, value = "_, _ -> new")
    static PlainMessageDialogBody plainMessage(final Component contents, final @Range(from = 1, to = 1024) int width) {
        return DialogInstancesProvider.instance().plainMessageDialogBody(contents, width);
    }
}
