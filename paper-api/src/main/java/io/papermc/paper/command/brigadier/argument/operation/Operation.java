package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreHolder;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * <p>
 * Represents a simple arithmetic operation between two integers.
 * An {@link Operation} backs an operator.
 * Most arithmetic operators (like {@code +=, -=, /=, etc.}) are
 * supported, alongside certain conditional operators ({@code >=, <=, ==, etc.}).
 * </p>
 * <p>
 * Note that conditional operators, instead of yielding a boolean value, return the value
 * that matches the operation.
 * For example, the {@code <=} operator always returns the <strong>smaller</strong> value
 * of two given values.
 * </p>
 */
@ApiStatus.Experimental
@NullMarked
public interface Operation {

    /**
     * <p>
     * Applies this operation to a pair of integers.
     * </p>
     * <p>
     * Arithmetic between two integers always follows this pattern: 
     * <pre>
     * return left &lt;operator&gt; right
     * </pre>
     * On certain operators, such as division, the order matters.
     * {@code 20 %= 10} yields a different result than{@code 10 %= 20}.
     * </p>
     *
     * @param left left side of the expression
     * @param right right side of the expression
     * @return result of the operation
     */
    int apply(int left, int right) throws CommandSyntaxException;

    /**
     * <p>
     * Applies this operation to a pair of {@link ScoreHolder}s, retrieving their
     * values from a {@link Scoreboard} and their respective {@link Objective}s.
     * </p>
     * <p>
     * Arithmetic between two integers always follows this pattern: 
     * <pre>return left &lt;operator&gt; right;</pre>
     * On certain operators, such as division, the order matters.
     * {@code 20 %= 10} yields a different result than{@code 10 %= 20}.
     * </p>
     *
     * @param scoreboard      scoreboard to edit the score of
     * @param sourceObjective objective to retrieve the score from
     * @param targetObjective objective to edit the score of
     * @param targetHolder    target of the operation
     * @param sourceHolder    source of the operation
     */
    void apply(Scoreboard scoreboard, Objective targetObjective, Objective sourceObjective, ScoreHolder targetHolder, ScoreHolder sourceHolder) throws CommandSyntaxException;
}
