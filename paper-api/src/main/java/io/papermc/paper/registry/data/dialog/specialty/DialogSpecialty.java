package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.set.RegistrySet;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import static net.kyori.adventure.text.Component.translatable;

/**
 * Represents a dialog specialty, which is a specific type of dialog that can be used in the Paper API.
 * Dialog specialties can be used to create various types of dialogs, such as confirmation dialogs, lists of dialogs,
 * multi-action dialogs, notices, and server links.
 */
public sealed interface DialogSpecialty permits ConfirmationSpecialty, DialogListSpecialty, MultiActionSpecialty, NoticeSpecialty, ServerLinksSpecialty {

    /**
     * Creates a confirmation specialty with the specified yes and no buttons.
     *
     * @param yesButton the button to confirm the action
     * @param noButton  the button to cancel the action
     * @return a new ConfirmationSpecialty instance
     */
    static ConfirmationSpecialty confirmation(final ActionButton yesButton, final ActionButton noButton) {
        return new ConfirmationSpecialtyImpl(yesButton, noButton);
    }

    /**
     * Creates a dialog list specialty with the specified dialogs, exit action, columns, and button width.
     *
     * @param dialogs      the set of dialogs to display
     * @param exitAction   the action button to exit the dialog
     * @param columns      the number of columns to display in the dialog
     * @param buttonWidth  the width of each button in the dialog
     * @return a new DialogListSpecialty instance
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static DialogListSpecialty dialogList(final RegistrySet<Dialog> dialogs, final @Nullable ActionButton exitAction, final int columns, final int buttonWidth) {
        return new DialogListSpecialtyImpl(dialogs, exitAction, columns, buttonWidth);
    }

    /**
     * Creates a dialog list builder with the specified dialogs.
     *
     * @param dialogs the set of dialogs to display
     * @return a new builder instance
     */
    @Contract(value = "_ -> new", pure = true)
    static DialogListSpecialty.Builder dialogList(final RegistrySet<Dialog> dialogs) {
        return new DialogListSpecialtyImpl.BuilderImpl(dialogs);
    }

    /**
     * Creates a multi-action specialty with the specified actions, exit action, and number of columns.
     *
     * @param actions      the list of action buttons to display
     * @param exitAction   the action button to exit the dialog
     * @param columns      the number of columns to display in the dialog
     * @return a new MultiActionSpecialty instance
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static MultiActionSpecialty multiAction(final List<ActionButton> actions, final @Nullable ActionButton exitAction, final int columns) {
        return new MultiActionSpecialtyImpl(actions, exitAction, columns);
    }

    /**
     * Creates a notice specialty with the default action button.
     *
     * @return a new NoticeSpecialty instance
     */
    @Contract(value = "-> new", pure = true)
    static NoticeSpecialty notice() {
        return new NoticeSpecialtyImpl(ActionButton.builder(translatable("gui.ok")).width(150).build());
    }

    /**
     * Creates a notice specialty with the specified action button.
     *
     * @param action the action button to display in the notice
     * @return a new NoticeSpecialty instance
     */
    @Contract(value = "_ -> new", pure = true)
    static NoticeSpecialty notice(final ActionButton action) {
        return new NoticeSpecialtyImpl(action);
    }

    /**
     * Creates a server links specialty with the specified exit action, number of columns, and button width.
     *
     * @param exitAction   the action button to exit the dialog
     * @param columns      the number of columns to display in the dialog
     * @param buttonWidth  the width of each button in the dialog
     * @return a new ServerLinksSpecialty instance
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static ServerLinksSpecialty serverLinks(final @Nullable ActionButton exitAction, final int columns, final int buttonWidth) {
        return new ServerLinksSpecialtyImpl(exitAction, columns, buttonWidth);
    }
}
