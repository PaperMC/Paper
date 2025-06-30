package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.registry.data.dialog.ActionButton;
import org.jspecify.annotations.Nullable;

record ServerLinksTypeImpl(
    @Nullable ActionButton exitAction,
    int columns,
    int buttonWidth
) implements ServerLinksType {
}
