package io.papermc.paper.command.brigadier.argument.resolvers;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.math.ColumnBlockPosition;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link ArgumentResolver} that's capable of resolving
 * a column position argument value using a {@link CommandSourceStack}.
 *
 * @see ArgumentTypes#columnBlockPosition() 
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ColumnBlockPositionResolver extends ArgumentResolver<ColumnBlockPosition> {
}
