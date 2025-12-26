package io.papermc.paper.persistence;

import org.jspecify.annotations.NullMarked;
import java.util.Optional;

/**
 * Represents the result of data-based operations like serialization, which
 * can be in either of three states. These different states are encoded as
 * records in this class in order to allow pattern matching over the result.
 * <p>
 * These different cases are:
 * <ul>
 *     <li>
 *         {@link Success} - the operation was executed without any errors
 *     </li>
 *     <li>
 *         {@link ErrorWithPartialResult} - an error occurred, but a
 *          partial result was produced (this might, for example, have
 *          unrecognized attributes missing)
 *     </li>
 *     <li>
 *         {@link Error} - a fatal error occurred preventing any type of
 *          result from being produced
 *     </li>
 * </ul>
 *
 * @param <R> type of the result
 */
@NullMarked
public sealed interface DataResult<R> {
    /**
     * Class used as a return value for data operations that were successful
     * and produced a result without errors.
     *
     * @param result result of the operation
     * @param <R> type of the result
     */
    @NullMarked
    record Success<R>(
        R result
    ) implements DataResult<R> {
        @Override
        public Optional<R> getResult() {
            return Optional.of(result);
        }

        @Override
        public Optional<R> getPartialResult() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getErrorMessage() {
            return Optional.empty();
        }
    }

    /**
     * Class used as a return value for data operations, which encountered
     * an error, but produced a partial result despite those errors.
     * This partial result might have attributes, which were not recognized
     * missing.
     * <p>
     * For example: If an item with a custom enchantment called "foo:bar"
     * was serialized and then deserialized in an environment, where "foo"
     * is no longer present, the partial result would be the item with
     * this specific enchantment removed.
     *
     * @param errorMessage error message encountered during the operation
     * @param partialResult partial result produced by the operation
     * @param <R> type of the partial result
     */
    @NullMarked
    record ErrorWithPartialResult<R>(
        String errorMessage,
        R partialResult
    ) implements DataResult<R> {
        @Override
        public Optional<R> getResult() {
            return Optional.empty();
        }

        @Override
        public Optional<R> getPartialResult() {
            return Optional.of(partialResult);
        }

        @Override
        public Optional<String> getErrorMessage() {
            return Optional.of(errorMessage);
        }
    }

    /**
     * Class used as a return value for data operations, which encountered
     * a fatal error, which prevented the operation from producing any
     * result at all.
     *
     * @param errorMessage error message encountered during the operation
     * @param <R> type of the partial result
     */
    @NullMarked
    record Error<R>(
        String errorMessage
    ) implements DataResult<R> {
        @Override
        public Optional<R> getResult() {
            return Optional.empty();
        }

        @Override
        public Optional<R> getPartialResult() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getErrorMessage() {
            return Optional.of(errorMessage);
        }
    }

    /**
     * Returns an {@link Optional} containing the result of the operation
     * if the operation was executed with no errors. Otherwise, the returned
     * {@link Optional} will be empty.
     *
     * @return the result if successful; empty optional otherwise
     */
    Optional<R> getResult();

    /**
     * Returns an {@link Optional} containing any result the operation might
     * have produced. This includes partial results, where an error occurred
     * during the operation, but a value was still produced.
     *
     * @return the result or partial result present; empty optional otherwise
     * @see ErrorWithPartialResult
     */
    Optional<R> getPartialResult();

    /**
     * Returns the error message describing what specifically went wrong
     * during the operation. Note, that even if an error occurred, a partial
     * result might have been produced (see {@link #getPartialResult()}).
     *
     * @return empty optional if the operation was successful; error message
     *  describing what went wrong otherwise
     */
    Optional<String> getErrorMessage();

    /**
     * Returns the result if the operation was executed successfully. If any
     * error occurred, an {@link IllegalStateException} with the appropriate
     * error message will be thrown.
     *
     * @return the result of the operation
     * @throws IllegalStateException if any error occurred during the operation
     */
    default R resultOrThrow() {
        return getResult().orElseThrow(() -> new IllegalStateException(getErrorMessage().orElseThrow()));
    }

    /**
     * Returns the result of the operation if it was executed successfully or
     * returns a partial result, produced despite an error. If not even a
     * partial result is available, throws an {@link IllegalStateException}.
     *
     * @return the result or partial result of the operation
     * @throws IllegalStateException if no partial result was produced
     * @see ErrorWithPartialResult
     */
    default R partialResultOrThrow() {
        return getPartialResult().orElseThrow(() -> new IllegalStateException(getErrorMessage().orElseThrow()));
    }
}
