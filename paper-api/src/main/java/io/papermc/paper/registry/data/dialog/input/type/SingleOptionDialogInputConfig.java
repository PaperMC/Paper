package io.papermc.paper.registry.data.dialog.input.type;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A configuration for a single option dialog input.
 */
public sealed interface SingleOptionDialogInputConfig extends DialogInputConfig permits SingleOptionDialogInputConfigImpl {

    /**
     * The width of the input.
     *
     * @return the width
     */
    @Contract(pure = true)
    int width();

    /**
     * The list of options for the input.
     *
     * @return the list of option entries
     */
    @Contract(pure = true)
    @Unmodifiable List<OptionEntry> entries();

    /**
     * The label for the input.
     *
     * @return the label component
     */
    @Contract(pure = true)
    Component label();

    /**
     * Whether the label should be visible.
     *
     * @return true if the label is visible, false otherwise
     */
    @Contract(pure = true)
    boolean labelVisible();

    /**
     * Represents a single option entry in a single option dialog input.
     */
    sealed interface OptionEntry permits SingleOptionDialogInputConfigImpl.SingleOptionEntryImpl {

        /**
         * Creates a new option entry.
         *
         * @param id the unique identifier for the option
         * @param display the display name for the option, or null if not set
         * @param initial whether this option is initially selected
         * @return a new option entry instance
         */
        @Contract(pure = true, value = "_, _, _ -> new")
        static OptionEntry create(final String id, final @Nullable Component display, final boolean initial) {
            return new SingleOptionDialogInputConfigImpl.SingleOptionEntryImpl(id, display, initial);
        }

        /**
         * The unique identifier for the option.
         *
         * @return the option ID
         */
        @Contract(pure = true)
        String id();

        /**
         * The display name for the option, or null if not set.
         *
         * @return the display component, or null
         */
        @Contract(pure = true)
        @Nullable Component display();

        /**
         * Whether this option is initially selected.
         * <p>Only 1 option is allowed to have initial selected.</p>
         *
         * @return true if the option is initially selected, false otherwise
         */
        @Contract(pure = true)
        boolean initial();
    }
}
