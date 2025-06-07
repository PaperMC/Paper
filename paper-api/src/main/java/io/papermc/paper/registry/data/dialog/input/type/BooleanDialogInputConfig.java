package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;

/**
 * A boolean dialog input configuration.
 * <p>Created via {@link DialogInputConfig#bool(Component, boolean, String, String)}</p>
 */
public sealed interface BooleanDialogInputConfig extends DialogInputConfig permits BooleanDialogInputConfigImpl {

    /**
     * The label for the input.
     *
     * @return the label component
     */
    @Contract(pure = true)
    Component label();

    /**
     * The initial value of the input.
     *
     * @return true if the input is initially true, false otherwise
     */
    @Contract(pure = true)
    boolean initial();

    /**
     * The input's value in a template when the value is true.
     *
     * @return the string to use when the input is true
     */
    @Contract(pure = true)
    String onTrue();

    /**
     * The input's value in a template when the value is false.
     *
     * @return the string to use when the input is false
     */
    @Contract(pure = true)
    String onFalse();

    /**
     * A builder for a boolean dialog input configuration.
     * <p>Created via {@link DialogInputConfig#bool(Component)}</p>
     */
    sealed interface Builder permits BooleanDialogInputConfigImpl.BuilderImpl {

        /**
         * Sets the initial value of the input.
         *
         * @param initial true if the input is initially true, false otherwise
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder initial(boolean initial);

        /**
         * Sets the input's value in a template when the value is true.
         *
         * @param onTrue the string to use when the input is true
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder onTrue(String onTrue);

        /**
         * Sets the input's value in a template when the value is false.
         *
         * @param onFalse the string to use when the input is false
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder onFalse(String onFalse);

        /**
         * Builds the instance with the configured values.
         *
         * @return a new instance
         */
        @Contract(value = "-> new", pure = true)
        BooleanDialogInputConfig build();
    }
}
