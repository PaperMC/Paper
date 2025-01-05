package io.papermc.paper.util;

import java.util.Optional;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * A type that can be either a left value or a right value.
 *
 * @param <L> the type of the left value
 * @param <R> the type of the right value
 */
@NullMarked
public sealed interface Either<L, R> permits Either.Left, Either.Right {

    /**
     * Create a new Either with a left value.
     *
     * @param value the left value
     * @return a new Either with a left value
     * @param <L> the type of the left value
     * @param <R> the type of the right value
     */
    @Contract(value = "_ -> new", pure = true)
    static <L, R> Either.Left<L, R> left(final L value) {
        return new EitherLeft<>(value);
    }

    /**
     * Create a new Either with a right value.
     *
     * @param value the right value
     * @return a new Either with a right value
     * @param <L> the type of the left value
     * @param <R> the type of the right value
     */
    @Contract(value = "_ -> new", pure = true)
    static <L, R> Either.Right<L, R> right(final R value) {
        return new EitherRight<>(value);
    }

    /**
     * Get an optional of the left value.
     *
     * @return an optional of the left value
     */
    Optional<L> left();

    /**
     * Get an optional of the right value.
     *
     * @return an optional of the right value
     */
    Optional<R> right();

    /**
     * A left value.
     *
     * @param <L> the type of the left value
     * @param <R> the type of the right value
     */
    sealed interface Left<L, R> extends Either<L, R> permits EitherLeft {

        /**
         * Get the left value.
         *
         * @return the left value
         */
        @Contract(pure = true)
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

    /**
     * A right value.
     *
     * @param <L> the type of the left value
     * @param <R> the type of the right value
     */
    sealed interface Right<L, R> extends Either<L, R> permits EitherRight {

        /**
         * Get the right value.
         *
         * @return the right value
         */
        @Contract(pure = true)
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
