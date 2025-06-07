package io.papermc.paper.dialog;

import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * A view for a possible response to a dialog.
 * There are no guarantees that this is an actual response to a
 * dialog form. It is on the plugin to validate that the response
 * is valid.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DialogResponseView {

    /**
     * Gets the raw payload of the response.
     *
     * @return the raw payload
     */
    @Contract(pure = true)
    BinaryTagHolder payload();

    /**
     * Gets a text value at a key.
     *
     * @param key the key
     * @return the value (or null if it doesn't exist)
     */
    @Contract(pure = true)
    @Nullable String getText(String key);

    /**
     * Gets a boolean value at a key.
     *
     * @param key the key
     * @return the value (or null if it doesn't exist)
     */
    @Contract(pure = true)
    @Nullable Boolean getBoolean(String key);

    /**
     * Gets a float value at a key.
     *
     * @param key the key
     * @return the value (or null if it doesn't exist)
     */
    @Contract(pure = true)
    @Nullable Float getFloat(String key);
}
