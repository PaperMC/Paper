package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.registry.data.dialog.ActionButton;
import org.jspecify.annotations.Nullable;

record ServerLinksSpecialtyImpl(
    @Nullable ActionButton exitAction,
    int columns,
    int buttonWidth
) implements ServerLinksSpecialty {
}
