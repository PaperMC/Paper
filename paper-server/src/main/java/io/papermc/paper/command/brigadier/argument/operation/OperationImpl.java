package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.commands.arguments.OperationArgument;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class OperationImpl implements Operation {

    private final Either<OperationArgument.SimpleOperation, OperationArgument.Operation> operationType;

    public OperationImpl(OperationArgument.SimpleOperation operation) {
        operationType = Either.left(operation);
    }

    public OperationImpl(OperationArgument.Operation operation) {
        operationType = Either.right(operation);
    }

    @Override
    public int apply(int left, int right) throws CommandSyntaxException {
        if (operationType.left().isEmpty()) {
            throw OperationArgument.ERROR_INVALID_OPERATION.create();
        }

        return operationType.left().get().apply(left, right);
    }

    public static Operation fromVanilla(OperationArgument.Operation vanillaOperation) {
        if (vanillaOperation instanceof OperationArgument.SimpleOperation simple) {
            return new OperationImpl(simple);
        }
        return new OperationImpl(vanillaOperation);
    }
}
