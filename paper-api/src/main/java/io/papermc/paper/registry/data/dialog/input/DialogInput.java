package io.papermc.paper.registry.data.dialog.input;

import io.papermc.paper.registry.data.dialog.input.type.DialogInputConfig;
import org.jetbrains.annotations.Contract;

/**
 * Represents a configured input for a dialog.
 */
public sealed interface DialogInput permits DialogInputImpl {

    /**
     * Creates a new dialog input with the specified key and input type.
     *
     * @param key        the key for this input
     * @param inputType  the type of this input
     * @return a new dialog input instance
     */
    @Contract(pure = true, value = "_, _ -> new")
    static DialogInput create(final String key, final DialogInputConfig inputType) {
        return new DialogInputImpl(key, inputType);
    }

    /**
     * The key for this input.
     * <p>Used in dialog actions to identify this dialog input's value</p>
     *
     * @return the key of this input
     */
    @Contract(pure = true)
    String key();

    /**
     * The type of this input.
     * <p>Used to determine how the input should be rendered and processed</p>
     *
     * @return the type of this input
     */
    @Contract(pure = true)
    DialogInputConfig inputType();
}
