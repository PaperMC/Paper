package io.papermc.paper.registry.data.dialog.action;

import io.papermc.paper.registry.data.dialog.DialogInstancesProvider;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * Represents an action that can be performed in a dialog.
 */
public sealed interface DialogAction permits DialogAction.CommandTemplateAction, DialogAction.CustomClickAction, DialogAction.StaticAction {

    /**
     * Creates a new command template action. The template format
     * looks for {@code $(variable_name)} to substitute variables.
     * {@code variable_name} should correspond to a {@link io.papermc.paper.registry.data.dialog.input.DialogInput#key()}.
     *
     * @param template the command template to execute
     * @return a new command template action instance
     */
    @Contract(pure = true, value = "_ -> new")
    static CommandTemplateAction commandTemplate(final String template) {
        return DialogInstancesProvider.instance().commandTemplate(template);
    }

    /**
     * Creates a new static action that performs a click event.
     *
     * @param value the click event to perform
     * @return a new static action instance
     */
    @Contract(pure = true, value = "_ -> new")
    static StaticAction staticAction(final ClickEvent value) {
        return DialogInstancesProvider.instance().staticAction(value);
    }

    /**
     * Creates a new custom click action that executes a custom action.
     * Each {@link io.papermc.paper.registry.data.dialog.input.DialogInput#key()} is added
     * to the compound binary tag holder.
     *
     * @param id the identifier of the custom action
     * @param additions additional data to be sent with the action, or null if not needed
     * @return a new custom click action instance
     */
    @Contract(pure = true, value = "_, _ -> new")
    static CustomClickAction customClick(final Key id, final @Nullable BinaryTagHolder additions) {
        return DialogInstancesProvider.instance().customClick(id, additions);
    }

    /**
     * Creates a new custom click action that executes a custom action.
     *
     * @param callback the custom action to execute
     * @param options the options for the custom action
     * @return a new custom click action instance
     */
    @Contract(pure = true, value = "_, _ -> new")
    static CustomClickAction customClick(final DialogActionCallback callback, final ClickCallback.Options options) {
        return DialogInstancesProvider.instance().register(callback, options);
    }

    /**
     * Represents an action that executes a command template.
     */
    @ApiStatus.NonExtendable
    non-sealed interface CommandTemplateAction extends DialogAction {

        /**
         * The command template to execute.
         *
         * @return the command template
         */
        @Contract(pure = true)
        String template();
    }

    /**
     * Represents an action that performs a static click event.
     */
    @ApiStatus.NonExtendable
    non-sealed interface StaticAction extends DialogAction {

        /**
         * The click event to perform.
         *
         * @return the click event
         */
        @Contract(pure = true)
        ClickEvent value();
    }

    /**
     * Represents an action that executes a custom action with additional data.
     */
    @ApiStatus.NonExtendable
    non-sealed interface CustomClickAction extends DialogAction {

        /**
         * The identifier of the custom action.
         *
         * @return the identifier
         */
        @Contract(pure = true)
        Key id();

        /**
         * Additional data to be sent with the action.
         * This is a compound binary tag holder that can contain
         * various data related to the action.
         *
         * @return the additional data, or null if not needed
         */
        @Contract(pure = true)
        @Nullable BinaryTagHolder additions();
    }
}
