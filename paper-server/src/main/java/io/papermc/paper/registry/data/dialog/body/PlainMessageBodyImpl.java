package io.papermc.paper.registry.data.dialog.body;

import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.body.PlainMessage;

import static io.papermc.paper.util.BoundChecker.requireRange;

public record PlainMessageBodyImpl(Component contents, int width) implements PlainMessageDialogBody {

    public PlainMessageBodyImpl {
        requireRange(width, "width", 1, 1024);
    }

    public PlainMessageBodyImpl(final Component contents) {
        this(contents, PlainMessage.DEFAULT_WIDTH);
    }
}
