package io.papermc.paper.registry.data.dialog.specialty;


import io.papermc.paper.registry.data.dialog.ActionButton;
import org.jspecify.annotations.Nullable;

public sealed interface ServerLinksSpecialty extends DialogSpecialty permits ServerLinksSpecialtyImpl {

    @Nullable ActionButton exitAction();

    int columns();

    int buttonWidth();
}
