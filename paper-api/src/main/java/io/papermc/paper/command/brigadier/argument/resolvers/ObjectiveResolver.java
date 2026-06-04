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
     * This method is the same as calling {@link #resolve(CommandSourceStack, Scoreboard, boolean)} with
     * the scoreboard retrieved from {@code Bukkit.getScoreboardManager().getMainScoreboard()} and {@code false}.
     *
     * @param sourceStack source stack
     * @return resolved objective
     */
    default Objective resolve(CommandSourceStack sourceStack) throws CommandSyntaxException {
        return resolve(sourceStack, Bukkit.getScoreboardManager().getMainScoreboard(), false);
    }

    /**
     * Resolves the argument with the given command source stack.
     * <p>
     * This method is the same as calling {@link #resolve(CommandSourceStack, Scoreboard, boolean)} with
     * the provided scoreboard and {@code false}.
     *
     * @param sourceStack source stack
     * @param scoreboard  scoreboard to get the objective from
     * @return resolved objective
     */
    default Objective resolve(CommandSourceStack sourceStack, Scoreboard scoreboard) throws CommandSyntaxException {
        return resolve(sourceStack, scoreboard, false);
    }

    /**
     * Resolves the argument with the given command source stack.
     * <p>
     * This method is the same as calling {@link #resolve(CommandSourceStack, Scoreboard, boolean)} with
     * the scoreboard retrieved from {@code Bukkit.getScoreboardManager().getMainScoreboard()}.
     *
     * @param sourceStack  source stack
     * @param onlyWritable whether to only retrieve modifiable objectives
     * @return resolved objective
     */
    default Objective resolve(CommandSourceStack sourceStack, boolean onlyWritable) throws CommandSyntaxException {
        return resolve(sourceStack, Bukkit.getScoreboardManager().getMainScoreboard(), onlyWritable);
    }

    /**
     * Resolves the argument with the given command source stack.
     *
     * @param sourceStack  source stack
     * @param scoreboard   scoreboard to get the objective from
     * @param onlyWritable whether to only retrieve modifiable objectives
     * @return resolved objective
     */
    Objective resolve(CommandSourceStack sourceStack, Scoreboard scoreboard, boolean onlyWritable) throws CommandSyntaxException;
}
