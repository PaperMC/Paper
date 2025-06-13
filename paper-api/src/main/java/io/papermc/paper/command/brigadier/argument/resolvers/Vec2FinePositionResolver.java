package io.papermc.paper.command.brigadier.argument.resolvers;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.math.Vec2FinePosition;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link ArgumentResolver} that's capable of resolving
 * a vec2 fine position argument value using a {@link CommandSourceStack}.
 *
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#vec2FinePosition()
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#vec2FinePosition(boolean)
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface Vec2FinePositionResolver extends ArgumentResolver<Vec2FinePosition> {
}
