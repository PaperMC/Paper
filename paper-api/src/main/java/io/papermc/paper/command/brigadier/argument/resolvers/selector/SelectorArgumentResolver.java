package io.papermc.paper.command.brigadier.argument.resolvers.selector;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.ArgumentResolver;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link ArgumentResolver} that's capable of resolving
 * a selector argument value using a {@link CommandSourceStack}.
 *
 * @param <T> resolved type
 * @see <a href="https://minecraft.wiki/w/Target_selectors">Target Selectors</a>
 */
@ApiStatus.NonExtendable
public interface SelectorArgumentResolver<T> extends ArgumentResolver<T> {
}
