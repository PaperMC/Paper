package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.registry.data.dialog.ActionButton;

public sealed interface NoticeSpecialty extends DialogSpecialty permits NoticeSpecialtyImpl {

    ActionButton action();
}
