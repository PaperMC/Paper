package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * Represents an action button in a dialog, which can be used to trigger actions or navigate within the dialog.
 * Action buttons can have labels, tooltips, and associated actions.
 */
public sealed interface ActionButton permits ActionButtonImpl {

    /**
     * Creates a new action button with the specified label, tooltip, width, and action.
     *
     * @param label   the label of the button
     * @param tooltip the tooltip to display when hovering over the button, or null if no tooltip is needed
     * @param width   the width of the button
     * @param action  the action to perform when the button is clicked, or null if no action is associated
     * @return a new ActionButton instance
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static ActionButton create(final Component label, final @Nullable Component tooltip, final int width, final @Nullable DialogAction action) {
        return new ActionButtonImpl(label, tooltip, width, action);
    }

    /**
     * Creates a new action button builder with the specified label.
     *
     * @param label the label of the button
     * @return a new ActionButton.Builder instance
     */
    @Contract(pure = true, value = "_ -> new")
    static ActionButton.Builder builder(final Component label) {
        return new ActionButtonImpl.BuilderImpl(label);
    }

    /**
     * Returns the label of the action button.
     *
     * @return the label of the button
     */
    @Contract(pure = true)
    Component label();

    /**
     * Returns the tooltip of the action button, or null if no tooltip is set.
     *
     * @return the tooltip of the button, or null
     */
    @Contract(pure = true)
    @Nullable Component tooltip();

    /**
     * Returns the width of the action button.
     *
     * @return the width of the button
     */
    @Contract(pure = true)
    int width();

    /**
     * Returns the action associated with this button, or null if no action is associated.
     *
     * @return the action to perform when the button is clicked, or null
     */
    @Contract(pure = true)
    @Nullable DialogAction action();

    /**
     * A builder for creating ActionButton instances.
     */
    sealed interface Builder permits ActionButtonImpl.BuilderImpl {

        /**
         * Sets the tooltip of the action button, or null if no tooltip is desired.
         *
         * @param tooltip the tooltip of the button, or null
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder tooltip(@Nullable Component tooltip);

        /**
         * Sets the width of the action button.
         *
         * @param width the width of the button
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder width(int width);

        /**
         * Sets the action associated with this button, or null if no action is desired.
         *
         * @param action the action to perform when the button is clicked, or null
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder action(@Nullable DialogAction action);

        /**
         * Builds the ActionButton instance with the configured values.
         *
         * @return a new ActionButton instance
         */
        @Contract(value = "-> new", pure = true)
        ActionButton build();
    }
}
