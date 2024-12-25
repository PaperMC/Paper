package io.papermc.paper.block;

import org.jspecify.annotations.NullMarked;

/**
 * Represents how the lid of a block behaves.
 */
@NullMarked
public enum LidMode {
    /**
     * The default lid mode, the lid will open and close based on player interaction.
     * <p>
     * the state used for this is provided with {@link Lidded#getTrueLidState()}
     */
    DEFAULT,

    /**
     * The lid will be forced open, regardless of player interaction.
     * <p>
     * This needs to be manually unset with another call to {@link Lidded#setLidMode(LidMode)}.
     */
    FORCED_OPEN,

    /**
     * The lid will be forced closed, regardless of player interaction.
     * <p>
     * This needs to be manually unset with another call to {@link Lidded#setLidMode(LidMode)}.
     */
    FORCED_CLOSED,

    /**
     * The lid will be forced open until at least one player has opened it.
     * <p>
     * It will then revert to {@link #DEFAULT}.
     * <p>
     * If at least one player is viewing it when this is set, it will immediately revert to
     * {@link #DEFAULT}.
     */
    OPEN_UNTIL_VIEWED,

    /**
     * The lid will be forced closed until all players currently viewing it have closed it.
     * <p>
     * It will then revert to {@link #DEFAULT}.
     * <p>
     * If no players are viewing it when this is set, it will immediately revert to
     * {@link #DEFAULT}.
     */
    CLOSED_UNTIL_NOT_VIEWED
}
