package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

/**
 * Represents the base of all dialogs.
 */
@ApiStatus.NonExtendable
public interface DialogBase {

    /**
     * Creates a new dialog base.
     *
     * @param title the title of the dialog
     * @param externalTitle the external title of the dialog, or null if not set
     * @param canCloseWithEscape if the dialog can be closed with the "escape" keybind
     * @param pause if the dialog should pause the game when opened (single-player only)
     * @param afterAction the action to take after the dialog is closed
     * @param body the body of the dialog
     * @param inputs the inputs of the dialog
     * @return a new dialog base instance
     */
    @Contract(value = "_, _, _, _, _, _, _ -> new", pure = true)
    static DialogBase create(
        final Component title,
        final @Nullable Component externalTitle,
        final boolean canCloseWithEscape,
        final boolean pause,
        final DialogAfterAction afterAction,
        final List<? extends DialogBody> body,
        final List<? extends DialogInput> inputs
    ) {
        return builder(title).externalTitle(externalTitle).canCloseWithEscape(canCloseWithEscape).pause(pause).afterAction(afterAction).body(body).inputs(inputs).build();
    }

    /**
     * Creates a new dialog base builder.
     *
     * @param title the title of the dialog
     * @return a new dialog base builder
     */
    @Contract(value = "_ -> new", pure = true)
    static Builder builder(final Component title) {
        return DialogInstancesProvider.instance().dialogBaseBuilder(title);
    }

    /**
     * The title of the dialog.
     *
     * @return the title
     */
    @Contract(pure = true)
    Component title();

    /**
     * The external title of the dialog. This title
     * is used on buttons that open this dialog.
     *
     * @return the external title or null
     */
    @Contract(pure = true)
    @Nullable Component externalTitle();

    /**
     * Returns if this dialog can be closed with the "escape" keybind.
     *
     * @return if the dialog can be closed with "escape"
     */
    @Contract(pure = true)
    boolean canCloseWithEscape();

    /**
     * Returns if this dialog should pause the game when opened (single-player only).
     *
     * @return if the dialog pauses the game
     */
    @Contract(pure = true)
    boolean pause();

    /**
     * The action to take after the dialog is closed.
     *
     * @return the action to take after the dialog is closed
     */
    @Contract(pure = true)
    DialogAfterAction afterAction();

    /**
     * The body of the dialog.
     * <p>
     * The body is a list of {@link DialogBody} elements that will be displayed in the dialog.
     *
     * @return the body of the dialog
     */
    @Contract(pure = true)
    @Unmodifiable List<DialogBody> body();

    /**
     * The inputs of the dialog.
     * <p>
     * The inputs are a list of {@link DialogInput} elements that will be displayed in the dialog.
     *
     * @return the inputs of the dialog
     */
    @Contract(pure = true)
    @Unmodifiable List<DialogInput> inputs();

    /**
     * Actions to take after the dialog is closed.
     */
    enum DialogAfterAction {
        /**
         * Closes the dialog and returns to the previous non-dialog screen (if any).
         */
        CLOSE("close"),
        /**
         * Does nothing (keeps the current screen open).
         */
        NONE("none"),
        /**
         * Replaces dialog with a "waiting for response" screen.
         */
        WAIT_FOR_RESPONSE("wait_for_response");

        public static final Index<String, DialogAfterAction> NAMES = Index.create(DialogAfterAction.class, e -> e.name);

        private final String name;

        DialogAfterAction(final String name) {
            this.name = name;
        }
    }

    /**
     * Builder interface for creating dialog bases.
     */
    @ApiStatus.NonExtendable
    interface Builder {

        /**
         * Sets the external title of the dialog.
         * This title is used on buttons that open this dialog.
         *
         * @param externalTitle the external title of the dialog, or null if not set
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder externalTitle(@Nullable Component externalTitle);

        /**
         * Sets whether the dialog can be closed with the "escape" keybind.
         *
         * @param canCloseWithEscape if the dialog can be closed with "escape"
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder canCloseWithEscape(boolean canCloseWithEscape);

        /**
         * Sets whether the dialog should pause the game when opened (single-player only).
         *
         * @param pause if the dialog should pause the game
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder pause(boolean pause);

        /**
         * Sets the action to take after the dialog is closed.
         *
         * @param afterAction the action to take after the dialog is closed
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder afterAction(DialogAfterAction afterAction);

        /**
         * Sets the body of the dialog.
         *
         * @param body the body of the dialog
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder body(List<? extends DialogBody> body);

        /**
         * Sets the inputs of the dialog.
         *
         * @param inputs the inputs of the dialog
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder inputs(List<? extends DialogInput> inputs);

        /**
         * Builds the dialog base.
         *
         * @return the built dialog base
         */
        @Contract(pure = true, value = "-> new")
        DialogBase build();
    }
}
