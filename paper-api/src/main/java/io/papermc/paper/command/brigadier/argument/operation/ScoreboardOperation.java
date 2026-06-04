package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a simple arithmetic operation between two integers.
 * A {@link ScoreboardOperation} backs an operator.
 * Most arithmetic operators (like {@code +=, -=, /=, etc.}) are
 * supported, alongside certain conditional operators ({@code >=, <=, ==, etc.}).
 * <p>
 * Note that conditional operators, instead of yielding a boolean value, return the value
 * that matches the operation.
 * For example, the {@code <=} operator always returns the <strong>smaller</strong> value
 * of two given values.
 */
@ApiStatus.Experimental
@NullMarked
public interface ScoreboardOperation {

    /**
     * Applies this operation to a pair of integers.
     * <p>
     * Arithmetic between two integers always follows this pattern:
     * <pre>
     * return left &lt;operator&gt; right
     * </pre>
     * On certain operators, such as division, the order matters.
     * {@code 20 %= 10} yields a different result than {@code 10 %= 20}.
     *
     * @param left  left side of the expression
     * @param right right side of the expression
     * @return result of this operation
     * @see Result
     */
    Result apply(int left, int right) throws CommandSyntaxException;

    /**
     * Represents the result of a {@link ScoreboardOperation}.
     * <p>
     * For arithmetical operations (+=, -=, /=, *=), {@link #result()} will yield the mathemtical result.
     * {@link #other()} will return the right input integer.
     * <p>
     * For any conditional operations or the swap operation, {@link #result()} will yield the left side
     * and {@link #other()} will yield the right side of the operation.
     */
    interface Result {

        /**
         * Get the left side of the result.
         *
         * @return result
         */
        int result();

        /**
         * Get the right side of the result.
         *
         * @return other
         */
        int other();
    }
}
