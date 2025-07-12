package io.papermc.paper.command.brigadier.argument.resolvers;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A resolver that's capable of resolving
 * an {@link Objective} value using a {@link Scoreboard} and {@link CommandSourceStack}.
 *
 * @see ArgumentTypes#objective()
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
@NullMarked
public interface ObjectiveResolver {

    /**
     * Resolves the argument with the given command source stack.
     * <p>
     * This method is the same as calling {@link #resolve(Scoreboard, CommandSourceStack)} with
     * the scoreboard retrieved from {@code Bukkit.getScoreboardManager().getMainScoreboard()}.
     * 
     * @param sourceStack source stack
     * @return resolved objective
     */
    default Objective resolve(CommandSourceStack sourceStack) throws CommandSyntaxException {
        return resolve(Bukkit.getScoreboardManager().getMainScoreboard(), sourceStack);
    }

    /**
     * Resolves the argument with the given command source stack.
     * <p>
     * This method is the same as calling {@link #resolve(Scoreboard, CommandSourceStack)} with
     * the scoreboard retrieved from {@code Bukkit.getScoreboardManager().getMainScoreboard()}.
     * 
     * @param sourceStack source stack
     * @return resolved objective, whose criteria is writable
     */
    default Objective resolveWritable(CommandSourceStack sourceStack) throws CommandSyntaxException {
        return resolveWritable(Bukkit.getScoreboardManager().getMainScoreboard(), sourceStack);
    }
    
    /**
     * Resolves the argument with the given command source stack.
     *
     * @param scoreboard  scoreboard to get the objective from
     * @param sourceStack source stack
     * @return resolved objective
     */
    Objective resolve(Scoreboard scoreboard, CommandSourceStack sourceStack) throws CommandSyntaxException;

    /**
     * Resolves the argument with the given command source stack.
     *
     * @param scoreboard  scoreboard to get the objective from
     * @param sourceStack source stack
     * @return resolved objective, whose criteria is writable
     */
    Objective resolveWritable(Scoreboard scoreboard, CommandSourceStack sourceStack) throws CommandSyntaxException;
}
