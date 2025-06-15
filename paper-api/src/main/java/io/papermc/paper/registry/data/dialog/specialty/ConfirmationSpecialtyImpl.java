package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.registry.data.dialog.ActionButton;

record ConfirmationSpecialtyImpl(ActionButton yesButton, ActionButton noButton) implements ConfirmationSpecialty {
}
