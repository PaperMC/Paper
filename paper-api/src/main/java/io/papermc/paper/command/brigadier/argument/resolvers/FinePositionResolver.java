package io.papermc.paper.command.brigadier.argument.resolvers;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.math.FinePosition;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link ArgumentResolver} that's capable of resolving
 * a fine position argument value using a {@link CommandSourceStack}.
 *
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#finePosition()
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#finePosition(boolean)
 */
@ApiStatus.NonExtendable
public interface FinePositionResolver extends ArgumentResolver<FinePosition> {
}
