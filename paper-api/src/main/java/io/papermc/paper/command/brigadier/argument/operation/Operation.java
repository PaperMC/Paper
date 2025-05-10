package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Operation {
    
    /**
     * Applies the given operation to a set of integers
     * @param left The left side of the operation
     * @param right The right side of the operation
     * @return The result of the operation
     */
    int apply(int left, int right) throws CommandSyntaxException;
}
