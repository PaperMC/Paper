package io.papermc.paper.command.brigadier.argument.predicate;

import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.block.Block;
import org.jetbrains.annotations.ApiStatus;

/**
 * A predicate for a {@link Block}.
 *
 * @see ArgumentTypes#blockInWorldPredicate()
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BlockInWorldPredicate {

    /**
     * Checks if the passed block matches the block predicate.
     * <p>
     * This method will load the chunk the block is at in a synchronous manner.
     *
     * @param block the block instance to check
     * @return {@code true} if the passed block instance matches this predicate, {@code false} otherwise
     */
    default boolean testBlock(final Block block) {
        return this.testBlock(block, true);
    }

    /**
     * Checks if the passed block matches the block predicate.
     *
     * @param block     the block instance to check
     * @param loadChunk if the chunk the block is located at should be loaded
     * @return {@code true} if the passed block instance matches this predicate, {@code false} otherwise
     */
    boolean testBlock(Block block, boolean loadChunk);
}
