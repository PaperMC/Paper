package io.papermc.paper.registry.data.dialog.specialty;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.data.dialog.ActionButton;
import java.util.List;
import org.jetbrains.annotations.Nullable;

record MultiActionSpecialtyImpl(
    List<ActionButton> actions,
    @Nullable ActionButton exitAction,
    int columns
) implements MultiActionSpecialty {

    MultiActionSpecialtyImpl {
        Preconditions.checkArgument(columns > 0, "columns must be greater than 0");
        Preconditions.checkArgument(!actions.isEmpty(), "actions cannot be empty");
        actions = List.copyOf(actions);
    }
}
