package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.set.RegistrySet;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import static net.kyori.adventure.text.Component.translatable;

/**
 * Represents a type of dialog.
 */
public sealed interface DialogType permits ConfirmationType, DialogListType, MultiActionType, NoticeType, ServerLinksType {

    /**
     * Creates a confirmation dialog with the specified yes and no buttons.
     *
     * @param yesButton the button to confirm the action
     * @param noButton  the button to cancel the action
     * @return a new instance
     */
    static ConfirmationType confirmation(final ActionButton yesButton, final ActionButton noButton) {
        return new ConfirmationTypeImpl(yesButton, noButton);
    }

    /**
     * Creates a dialog list dialog with the specified dialogs, exit action, columns, and button width.
     *
     * @param dialogs      the set of dialogs to display
     * @param exitAction   the action button to exit the dialog
     * @param columns      the number of columns to display in the dialog
     * @param buttonWidth  the width of each button in the dialog
     * @return a new instance
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static DialogListType dialogList(final RegistrySet<Dialog> dialogs, final @Nullable ActionButton exitAction, final int columns, final int buttonWidth) {
        return new DialogListTypeImpl(dialogs, exitAction, columns, buttonWidth);
    }

    /**
     * Creates a dialog list builder with the specified dialogs.
     *
     * @param dialogs the set of dialogs to display
     * @return a new builder instance
     */
    @Contract(value = "_ -> new", pure = true)
    static DialogListType.Builder dialogList(final RegistrySet<Dialog> dialogs) {
        return new DialogListTypeImpl.BuilderImpl(dialogs);
    }

    /**
     * Creates a multi-action dialog with the specified actions, exit action, and number of columns.
     *
     * @param actions      the list of action buttons to display
     * @param exitAction   the action button to exit the dialog
     * @param columns      the number of columns to display in the dialog
     * @return a new instance
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static MultiActionType multiAction(final List<ActionButton> actions, final @Nullable ActionButton exitAction, final int columns) {
        return new MultiActionTypeImpl(actions, exitAction, columns);
    }

    /**
     * Creates a notice dialog with the default action button.
     *
     * @return a new instance
     */
    @Contract(value = "-> new", pure = true)
    static NoticeType notice() {
        return new NoticeTypeImpl(ActionButton.builder(translatable("gui.ok")).width(150).build());
    }

    /**
     * Creates a notice dialog with the specified action button.
     *
     * @param action the action button to display in the notice
     * @return a new instance
     */
    @Contract(value = "_ -> new", pure = true)
    static NoticeType notice(final ActionButton action) {
        return new NoticeTypeImpl(action);
    }

    /**
     * Creates a server links dialog with the specified exit action, number of columns, and button width.
     *
     * @param exitAction   the action button to exit the dialog
     * @param columns      the number of columns to display in the dialog
     * @param buttonWidth  the width of each button in the dialog
     * @return a new instance
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static ServerLinksType serverLinks(final @Nullable ActionButton exitAction, final int columns, final int buttonWidth) {
        return new ServerLinksTypeImpl(exitAction, columns, buttonWidth);
    }
}
