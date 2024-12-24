package io.papermc.paper.util;

import java.util.Optional;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
public sealed interface Either<L, R> permits Either.Left, Either.Right {

    @Contract(value = "_ -> new", pure = true)
    static <L, R> Either.Left<L, R> left(final L value) {
        return new EitherLeft<>(value);
    }

    @Contract(value = "_ -> new", pure = true)
    static <L, R> Either.Right<L, R> right(final R value) {
        return new EitherRight<>(value);
    }

    Optional<L> left();

    Optional<R> right();

    sealed interface Left<L, R> extends Either<L, R> permits EitherLeft {
        L value();

        @Override
        default Optional<L> left() {
            return Optional.of(this.value());
        }

        @Override
        default Optional<R> right() {
            return Optional.empty();
        }
    }

    sealed interface Right<L, R> extends Either<L, R> permits EitherRight {
        R value();

        @Override
        default Optional<L> left() {
            return Optional.empty();
        }

        @Override
        default Optional<R> right() {
            return Optional.of(this.value());
        }
    }
}
