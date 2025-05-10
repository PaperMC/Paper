package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreHolder;
import org.bukkit.scoreboard.Scoreboard;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Operation {

    /**
     * Applies the given operation to a set of integers
     *
     * @param left  The left side of the operation
     * @param right The right side of the operation
     * @return The result of the operation
     */
    int apply(int left, int right) throws CommandSyntaxException;

    /**
     * Applies the given operation to a set of score holders
     *
     * @param scoreboard      The scoreboard to edit the score of
     * @param sourceObjective The objective to retrieve the score from
     * @param targetObjective The objective to edit the score of
     * @param targetHolder    The target of the operation
     * @param sourceHolder    The source of the operation
     */
    void apply(Scoreboard scoreboard, Objective targetObjective, Objective sourceObjective, ScoreHolder targetHolder, ScoreHolder sourceHolder) throws CommandSyntaxException;
}
