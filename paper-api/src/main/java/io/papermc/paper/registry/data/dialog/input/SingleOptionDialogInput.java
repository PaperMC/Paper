package io.papermc.paper.registry.data.dialog.input;

import io.papermc.paper.registry.data.dialog.DialogInstancesProvider;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

/**
 * A single option dialog input.
 * <p>Created via {@link DialogInput#singleOption(String, int, List, Component, boolean)}</p>
 */
@ApiStatus.NonExtendable
public non-sealed interface SingleOptionDialogInput extends DialogInput {

    /**
     * The width of the input.
     *
     * @return the width
     */
    @Contract(pure = true)
    @Range(from = 1, to = 1024) int width();

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
     * <p>Only 1 option is allowed to have initial selected.</p>
     */
    @ApiStatus.NonExtendable
    interface OptionEntry {

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
            return DialogInstancesProvider.instance().singleOptionEntry(id, display, initial);
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

    /**
     * A builder for creating a {@link SingleOptionDialogInput}.
     * <p>Created via {@link DialogInput#singleOption(String, Component, List)}</p>
     */
    @ApiStatus.NonExtendable
    interface Builder {

        /**
         * Sets the width of the input.
         *
         * @param width the width
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder width(@Range(from = 1, to = 1024) int width);

        /**
         * Sets whether the label should be visible.
         *
         * @param labelVisible whether the label should be visible
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder labelVisible(boolean labelVisible);

        /**
         * Builds the {@link SingleOptionDialogInput}.
         *
         * @return the built dialog input
         */
        @Contract(value = "-> new", pure = true)
        SingleOptionDialogInput build();
    }
}
