package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.registry.data.dialog.ActionButton;
import java.util.List;
import org.jetbrains.annotations.Nullable;

record MultiActionSpecialtyImpl(
    List<ActionButton> actions,
    @Nullable ActionButton exitAction,
    int columns
) implements MultiActionSpecialty {

    MultiActionSpecialtyImpl {
        actions = List.copyOf(actions);
    }
}
