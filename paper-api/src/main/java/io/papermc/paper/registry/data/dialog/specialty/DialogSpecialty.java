package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.set.RegistrySet;
import java.util.List;
import org.jspecify.annotations.Nullable;

public sealed interface DialogSpecialty permits ConfirmationSpecialty, DialogListSpecialty, MultiActionSpecialty, NoticeSpecialty, ServerLinksSpecialty {

    static ConfirmationSpecialty confirmation(final ActionButton yesButton, final ActionButton noButton) {
        return new ConfirmationSpecialtyImpl(yesButton, noButton);
    }

    static DialogListSpecialty dialogList(final RegistrySet<Dialog> dialogs, final @Nullable ActionButton exitAction, final int columns, final int buttonWidth) {
        return new DialogListSpecialtyImpl(dialogs, exitAction, columns, buttonWidth);
    }

    static MultiActionSpecialty multiAction(final List<ActionButton> actions, final @Nullable ActionButton exitAction, final int columns) {
        return new MultiActionSpecialtyImpl(actions, exitAction, columns);
    }

    static NoticeSpecialty notice(final ActionButton action) {
        return new NoticeSpecialtyImpl(action);
    }

    static ServerLinksSpecialty serverLinks(final @Nullable ActionButton exitAction, final int columns, final int buttonWidth) {
        return new ServerLinksSpecialtyImpl(exitAction, columns, buttonWidth);
    }
}
