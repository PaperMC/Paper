package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.registry.data.dialog.ActionButton;

public sealed interface ConfirmationSpecialty extends DialogSpecialty permits ConfirmationSpecialtyImpl {

    ActionButton yesButton();

    ActionButton noButton();
}
