package io.papermc.paper.command.brigadier.argument.resolvers;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector2f;

/**
 * An {@link ArgumentResolver} that's capable of resolving
 * a rotation argument value using a {@link CommandSourceStack}.
 *
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#rotation()
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface RotationResolver extends ArgumentResolver<Vector2f> {
}
