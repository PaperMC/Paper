package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreHolder;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a simple arithmetic operation between two integers.
 */
@ApiStatus.Experimental
@NullMarked
public interface Operation {

    /**
     * Applies this operation to a set of integers
     *
     * @param left  The left side of the expression
     * @param right The right side of the expression
     * @return The result of the operation
     */
    int apply(int left, int right) throws CommandSyntaxException;

    /**
     * Applies this operation to a set of score holders
     *
     * @param scoreboard      The scoreboard to edit the score of
     * @param sourceObjective The objective to retrieve the score from
     * @param targetObjective The objective to edit the score of
     * @param targetHolder    The target of the operation
     * @param sourceHolder    The source of the operation
     */
    void apply(Scoreboard scoreboard, Objective targetObjective, Objective sourceObjective, ScoreHolder targetHolder, ScoreHolder sourceHolder) throws CommandSyntaxException;
}
