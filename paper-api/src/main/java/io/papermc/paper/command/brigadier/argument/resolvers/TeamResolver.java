package io.papermc.paper.command.brigadier.argument.resolvers;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A resolver that's capable of resolving
 * a {@link Team} value using a {@link Scoreboard} and {@link CommandSourceStack}.
 *
 * @see ArgumentTypes#team()
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
@NullMarked
public interface TeamResolver {

    /**
     * Resolves the argument with the given command source stack.
     * <p>
     * This method is the same as calling {@link #resolve(Scoreboard, CommandSourceStack)} with
     * the scoreboard retrieved from {@code Bukkit.getScoreboardManager().getMainScoreboard()}.
     *
     * @param sourceStack source stack
     * @return resolved team
     * @see #resolve(Scoreboard, CommandSourceStack)
     * @see ScoreboardManager#getMainScoreboard()
     */
    default Team resolve(CommandSourceStack sourceStack) throws CommandSyntaxException {
        return resolve(Bukkit.getScoreboardManager().getMainScoreboard(), sourceStack);
    }

    /**
     * Resolves the argument with the given scoreboard and command source stack.
     *
     * @param scoreboard  scoreboard to get the team from
     * @param sourceStack source stack
     * @return resolved team
     */
    Team resolve(Scoreboard scoreboard, CommandSourceStack sourceStack) throws CommandSyntaxException;
}
