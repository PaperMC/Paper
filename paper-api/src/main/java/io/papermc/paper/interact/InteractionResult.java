package io.papermc.paper.interact;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents the result of an interaction action.
 */
@NullMarked
@ApiStatus.NonExtendable
public sealed interface InteractionResult {

    /**
     * Whether this result is considered a terminal action. If <code>false</code>, further interactions may follow.
     *
     * @return if this result is a terminal action
     */
    boolean consumesAction();

    @ApiStatus.NonExtendable
    non-sealed interface Success extends InteractionResult {

        /**
         * Returns the source of the resulting arm swing, if any.
         *
         * @return the swing source
         */
        SwingSource swingSource();

        /**
         * Returns additional context for successful item-related interactions, if any.
         *
         * @return the item context
         */
        ItemContext itemContext();

    }

    /**
     * Denotes a failed interaction.
     */
    @ApiStatus.NonExtendable
    non-sealed interface Fail extends InteractionResult {

    }

    /**
     * Denotes an interaction where no action had been performed.
     */
    @ApiStatus.NonExtendable
    non-sealed interface Pass extends InteractionResult {

    }

    /**
     * Denotes that another interaction will be performed for default block behavior, without an item.
     */
    @ApiStatus.NonExtendable
    non-sealed interface TryEmptyHandInteraction extends InteractionResult {

    }

}
