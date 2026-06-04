package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.OperationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.world.scores.ScoreAccess;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ScoreboardOperationImpl implements ScoreboardOperation {

    private final OperationArgument.Operation operation;

    public ScoreboardOperationImpl(OperationArgument.Operation operation) {
        this.operation = operation;
    }

    @Override
    public Result apply(final int left, final int right) throws CommandSyntaxException {
        ScoreAccess leftScoreAccess = new SimpleScoreAccess(left);
        ScoreAccess rightScoreAccess = new SimpleScoreAccess(right);

        this.operation.apply(leftScoreAccess, rightScoreAccess);

        return new OperationResultImpl(leftScoreAccess.get(), rightScoreAccess.get());
    }

    private static final class SimpleScoreAccess implements ScoreAccess {

        private int value;

        public SimpleScoreAccess(final int value) {
            this.value = value;
        }

        @Override
        public int get() {
            return this.value;
        }

        @Override
        public void set(final int value) {
            this.value = value;
        }

        @Override
        public boolean locked() {
            return false;
        }

        @Override
        public void unlock() {
            // Not implemented
        }

        @Override
        public void lock() {
            // Not implemented
        }

        @Override
        public @Nullable Component display() {
            return null;
        }

        @Override
        public void display(@Nullable final Component value) {
            // Not implemented
        }

        @Override
        public void numberFormatOverride(@Nullable final NumberFormat format) {
            // Not implemented
        }
    }

    private record OperationResultImpl(
        int result,
        int other
    ) implements Result {}
}
