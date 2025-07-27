package io.papermc.paper.command.brigadier.argument.resolvers;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.position.ColumnFinePosition;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link ArgumentResolver} that's capable of resolving
 * a column fine position argument value using a {@link CommandSourceStack}.
 *
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#columnFinePosition()
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#columnFinePosition(boolean)
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ColumnFinePositionResolver extends ArgumentResolver<ColumnFinePosition> {
}
