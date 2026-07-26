package io.papermc.paper.command.brigadier.argument.resolvers;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link ArgumentResolver} is capable of resolving
 * an argument value using a {@link CommandSourceStack}.
 *
 * @param <T> resolved type
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes
 */
@ApiStatus.NonExtendable
public interface ArgumentResolver<T> {

    /**
     * Resolves the argument with the given
     * command source stack.
     * @param sourceStack source stack
     * @return resolved
     */
    T resolve(CommandSourceStack sourceStack) throws CommandSyntaxException;
}
