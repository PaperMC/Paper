package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.registry.data.dialog.ActionButton;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requirePositive;
import static io.papermc.paper.util.BoundChecker.requireRange;

public record ServerLinksTypeImpl(
    @Nullable ActionButton exitAction,
    int columns,
    int buttonWidth
) implements ServerLinksType {

    public ServerLinksTypeImpl {
        requirePositive(columns, "columns");
        requireRange(buttonWidth, "buttonWidth", 1, 1024);
    }
}
