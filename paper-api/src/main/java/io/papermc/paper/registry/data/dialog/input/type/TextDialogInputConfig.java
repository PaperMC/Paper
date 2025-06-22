package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

/**
 * A text dialog input configuration.
 */
public sealed interface TextDialogInputConfig extends DialogInputConfig permits TextDialogInputConfigImpl {

    /**
     *
     * @return
     */
    int width();

    /**
     * The label for the input.
     *
     * @return the label component
     */
    Component label();

    /**
     * Whether the label should be visible.
     *
     * @return true if the label is visible, false otherwise
     */
    boolean labelVisible();

    /**
     * The initial value of the input.
     *
     * @return the initial text
     */
    String initial();

    /**
     * The format for the label (a translation key or format string).
     *
     * @return the label format
     */
    int maxLength();

    /**
     * The multiline options for the input, or null if not set.
     *
     * @return the multiline options
     */
    @Nullable MultilineOptions multiline();

    /**
     * Represents the multiline options for a text dialog input.
     */
    sealed interface MultilineOptions permits MultilineOptionsImpl {

        /**
         * Creates a new multiline options instance.
         *
         * @param maxLines the maximum number of lines, or null if not set
         * @param height the height of the input, or null if not set
         * @return a new MultilineOptions instance
         */
        static MultilineOptions create(final @Nullable Integer maxLines, final @Nullable Integer height) {
            return new MultilineOptionsImpl(maxLines, height);
        }

        @Nullable Integer maxLines();

        @Nullable Integer height();
    }
}
