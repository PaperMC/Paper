package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;

/**
 * A boolean dialog input configuration.
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
}
