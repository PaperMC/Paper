package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.registry.data.dialog.ActionButton;

public record ConfirmationTypeImpl(ActionButton yesButton, ActionButton noButton) implements ConfirmationType {
}
