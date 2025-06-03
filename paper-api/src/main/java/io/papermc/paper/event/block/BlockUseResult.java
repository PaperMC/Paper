package io.papermc.paper.event.block;

/**
 * Represents the outcome of a block use interaction.
 */
public enum BlockUseResult {

    /**
     * The block has successfully handled the interaction.
     * No further logic (e.g., item use) will occur.
     */
    SUCCESS,

    /**
     * The block did not handle the interaction.
     * Interaction should proceed with post-block logic,
     * such as using the item in hand (e.g., placing a block).
     */
    PASS,

    /**
     * The interaction was blocked or failed.
     */
    FAIL,

    /**
     * The interaction should be reattempted with an empty hand.
     */
    TRY_WITH_EMPTY_HAND

}
