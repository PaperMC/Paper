package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.set.RegistrySet;
import org.jspecify.annotations.Nullable;

public sealed interface DialogListSpecialty extends DialogSpecialty permits DialogListSpecialtyImpl {

    RegistrySet<Dialog> dialogs();

    @Nullable ActionButton exitAction();

    int columns();

    int buttonWidth();
}
