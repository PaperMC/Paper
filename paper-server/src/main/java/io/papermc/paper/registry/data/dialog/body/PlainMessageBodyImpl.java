package io.papermc.paper.registry.data.dialog.body;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.body.PlainMessage;

public record PlainMessageBodyImpl(Component contents, int width) implements PlainMessageDialogBody {

    public PlainMessageBodyImpl {
        Preconditions.checkArgument(width >= 1 && width <= 1024, "Width must be between 1 and 1024");
    }

    public PlainMessageBodyImpl(final Component contents) {
        this(contents, PlainMessage.DEFAULT_WIDTH);
    }
}
