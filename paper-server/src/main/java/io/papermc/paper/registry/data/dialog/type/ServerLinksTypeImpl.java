package io.papermc.paper.registry.data.dialog.type;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.data.dialog.ActionButton;
import org.jspecify.annotations.Nullable;

public record ServerLinksTypeImpl(
    @Nullable ActionButton exitAction,
    int columns,
    int buttonWidth
) implements ServerLinksType {

    public ServerLinksTypeImpl {
        Preconditions.checkArgument(columns > 0, "columns must be greater than 0");
        Preconditions.checkArgument(buttonWidth >= 1 && buttonWidth <= 1024, "buttonWidth must be between 1 and 1024");
    }
}
