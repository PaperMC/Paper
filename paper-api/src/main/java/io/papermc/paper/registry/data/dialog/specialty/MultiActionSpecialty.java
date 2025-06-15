package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.registry.data.dialog.ActionButton;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public sealed interface MultiActionSpecialty extends DialogSpecialty permits MultiActionSpecialtyImpl {

    @Unmodifiable List<ActionButton> actions();

    @Nullable ActionButton exitAction();

    int columns();
}
