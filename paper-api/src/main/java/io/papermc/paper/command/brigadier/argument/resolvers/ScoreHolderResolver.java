package io.papermc.paper.command.brigadier.argument.resolvers;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.scoreboard.ScoreHolder;
import org.jetbrains.annotations.ApiStatus;
import java.util.Collection;
import java.util.List;

/**
 * An {@link ArgumentResolver} that's capable of resolving
 * an argument value using a {@link CommandSourceStack} into a
 * list of {@link ScoreHolder}s.
 *
 * @see ArgumentTypes#scoreHolders() 
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ScoreHolderResolver extends ArgumentResolver<List<ScoreHolder>> {
}
