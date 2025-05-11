package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a simple arithmetic operation between two integers.
 * An {@link Operation} backs an operator.
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
public interface Operation {

    /**
     * Applies this operation to a pair of integers.
     * <p>
     * Arithmetic between two integers always follows this pattern: 
     * <pre>
     * return left &lt;operator&gt; right
     * </pre>
     * On certain operators, such as division, the order matters.
     * {@code 20 %= 10} yields a different result than{@code 10 %= 20}.
     *
     * @param left left side of the expression
     * @param right right side of the expression
     * @return result of this operation
     */
    IntIntPair apply(int left, int right) throws CommandSyntaxException;

    /**
     * Applies this operation to a pair of integers.
     * <p>
     * Arithmetic between two integers always follows this pattern: 
     * <pre>
     * return left &lt;operator&gt; right
     * </pre>
     * On certain operators, such as division, the order matters.
     * {@code 20 %= 10} yields a different result than{@code 10 %= 20}.
     *
     * @param left left side of the expression
     * @param right right side of the expression
     * @return the left side of the result of this operation
     * @see #apply(int, int) 
     */
    default int applyLeftSide(int left, int right) throws CommandSyntaxException {
        return apply(left, right).leftInt();
    }
}
