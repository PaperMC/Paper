package io.papermc.paper.command.brigadier.argument.resolvers;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.scoreboard.ScoreHolder;
import org.jetbrains.annotations.ApiStatus;
import java.util.Collection;

/**
 * An {@link ArgumentResolver} that's capable of resolving
 * argument value using a {@link CommandSourceStack}.
 *
 * @see ArgumentTypes#scoreHolders() 
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ScoreHolderResolver extends ArgumentResolver<Collection<ScoreHolder>> {
}
