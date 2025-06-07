package io.papermc.paper.registry.data.dialog.body;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public sealed interface DialogBody permits DialogBody.ItemBody, DialogBody.PlainMessageBody {

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

    static PlainMessageBody plainMessage(final Component contents, final int width) {
        return new PlainMessageBodyImpl(contents, width);
    }

    sealed interface ItemBody extends DialogBody permits ItemBodyImpl {

        ItemStack item();

        @Nullable PlainMessageBody description();

        boolean showDecorations();

        boolean showTooltip();

        int width();

        int height();
    }

    sealed interface PlainMessageBody extends DialogBody permits PlainMessageBodyImpl {

        Component contents();

        int width();
    }
}
