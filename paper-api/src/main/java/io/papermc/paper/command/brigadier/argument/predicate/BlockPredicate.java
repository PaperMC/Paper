package io.papermc.paper.command.brigadier.argument.predicate;


import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import java.util.function.Predicate;
import org.bukkit.block.Block;
import org.jetbrains.annotations.ApiStatus;

/**
 * A predicate for a {@link org.bukkit.block.Block}.
 *
 * @see ArgumentTypes#blockPredicate()
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BlockPredicate extends Predicate<Block> {
}
