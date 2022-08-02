package io.papermc.paper.command.brigadier.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * An argument type that wraps around a native-to-vanilla argument type.
 * This argument receives special handling in that the native argument type will
 * be sent to the client for possible client-side completions and syntax validation.
 * <p>
 * When implementing this class, you have to create your own parsing logic from a
 * {@link StringReader}. If only want to convert from the native type ({@code N}) to the custom
 * type ({@code T}), implement {@link Converted} instead.
 *
 * @param <T> custom type
 * @param <N> type with an argument native to vanilla Minecraft (from {@link ArgumentTypes})
 */
@ApiStatus.Experimental
@NullMarked
public interface CustomArgumentType<T, N> extends ArgumentType<T> {

    /**
     * Parses the argument into the custom type ({@code T}). Keep in mind
     * that this parsing will be done on the server. This means that if
     * you throw a {@link CommandSyntaxException} during parsing, this
     * will only show up to the user after the user has executed the command
     * not while they are still entering it.
     *
     * @param reader string reader input
     * @return parsed value
     * @throws CommandSyntaxException if an error occurs while parsing
     */
    @Override
    T parse(final StringReader reader) throws CommandSyntaxException;

    /**
     * Gets the native type that this argument uses,
     * the type that is sent to the client.
     *
     * @return native argument type
     */
    ArgumentType<N> getNativeType();

    /**
     * Cannot be controlled by the server.
     * Returned in cases where there are multiple arguments in the same node.
     * This helps differentiate and tell the player what the possible inputs are.
     *
     * @return client set examples
     */
    @Override
    @ApiStatus.NonExtendable
    default Collection<String> getExamples() {
        return this.getNativeType().getExamples();
    }

    /**
     * Provides a list of suggestions to show to the client.
     *
     * @param context command context
     * @param builder suggestion builder
     * @return suggestions
     * @param <S> context type
     */
    @Override
    default <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return ArgumentType.super.listSuggestions(context, builder);
    }

    /**
     * An argument type that wraps around a native-to-vanilla argument type.
     * This argument receives special handling in that the native argument type will
     * be sent to the client for possible client-side completions and syntax validation.
     * <p>
     * The parsed native type will be converted via {@link #convert(Object)}.
     * Implement {@link CustomArgumentType} if you want to handle parsing the type manually.
     *
     * @param <T> custom type
     * @param <N> type with an argument native to vanilla Minecraft (from {@link ArgumentTypes})
     */
    @ApiStatus.Experimental
    interface Converted<T, N> extends CustomArgumentType<T, N> {

        @ApiStatus.NonExtendable
        @Override
        default T parse(final StringReader reader) throws CommandSyntaxException {
            return this.convert(this.getNativeType().parse(reader));
        }

        /**
         * Converts the value from the native type to the custom argument type.
         *
         * @param nativeType native argument provided value
         * @return converted value
         * @throws CommandSyntaxException if an exception occurs while parsing
         */
        T convert(N nativeType) throws CommandSyntaxException;
    }
}
