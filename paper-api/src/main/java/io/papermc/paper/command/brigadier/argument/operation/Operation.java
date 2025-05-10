package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
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
     * @param target          The target of the operation
     * @param sourcePlayer    The source of the operation
     */
    void apply(Scoreboard scoreboard, Objective targetObjective, Objective sourceObjective, String target, OfflinePlayer sourcePlayer) throws CommandSyntaxException;

    /**
     * Applies the given operation to a set of score holders
     *
     * @param scoreboard      The scoreboard to edit the score of
     * @param sourceObjective The objective to retrieve the score from
     * @param targetObjective The objective to edit the score of
     * @param targetPlayer    The target of the operation
     * @param source          The source of the operation
     */
    void apply(Scoreboard scoreboard, Objective targetObjective, Objective sourceObjective, OfflinePlayer targetPlayer, String source) throws CommandSyntaxException;

    /**
     * Applies the given operation to a set of score holders
     *
     * @param scoreboard      The scoreboard to edit the score of
     * @param sourceObjective The objective to retrieve the score from
     * @param targetObjective The objective to edit the score of
     * @param target          The target of the operation
     * @param source          The source of the operation
     */
    void apply(Scoreboard scoreboard, Objective targetObjective, Objective sourceObjective, String target, String source) throws CommandSyntaxException;

    /**
     * Applies the given operation to a set of score holders
     *
     * @param scoreboard      The scoreboard to edit the score of
     * @param sourceObjective The objective to retrieve the score from
     * @param targetObjective The objective to edit the score of
     * @param targetPlayer    The target of the operation
     * @param sourcePlayer    The source of the operation
     */
    void apply(Scoreboard scoreboard, Objective targetObjective, Objective sourceObjective, OfflinePlayer targetPlayer, OfflinePlayer sourcePlayer) throws CommandSyntaxException;
}
