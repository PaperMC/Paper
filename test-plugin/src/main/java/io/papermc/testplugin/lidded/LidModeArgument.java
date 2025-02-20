package io.papermc.testplugin.lidded;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.block.LidMode;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class LidModeArgument implements CustomArgumentType.Converted<LidMode, String> {

    private static final SimpleCommandExceptionType INVALID_MODE = new SimpleCommandExceptionType(
        new LiteralMessage("Invalid lid mode"));

    private static final LinkedHashMap<String, LidMode> BY_STRING = Arrays.stream(
            LidMode.values())
        .collect(
            Collectors.toMap(LidMode::name, Function.identity(), (lidMode, lidMode2) -> lidMode,
                LinkedHashMap::new));

    static boolean matchesSubStr(String remaining, String candidate) {
        for (int i = 0; !candidate.startsWith(remaining, i); i++) {
            int j = candidate.indexOf(46, i);
            int k = candidate.indexOf(95, i);
            if (Math.max(j, k) < 0) {
                return false;
            }

            if (j >= 0 && k >= 0) {
                i = Math.min(k, j);
            } else {
                i = j >= 0 ? j : k;
            }
        }

        return true;
    }

    public static LidModeArgument lidMode() {
        return new LidModeArgument();
    }

    public static LidMode getLidMode(CommandContext<?> context, String name) {
        return context.getArgument(name, LidMode.class);
    }

    @Override
    public @NotNull LidMode convert(@NotNull final String nativeType)
        throws CommandSyntaxException {
        LidMode mode = BY_STRING.get(nativeType.toUpperCase(Locale.ROOT));
        if (mode == null) {
            throw INVALID_MODE.create();
        }
        return mode;
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public @NotNull Collection<String> getExamples() {
        return BY_STRING.keySet();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(
        final @NotNull CommandContext<S> context, final @NotNull SuggestionsBuilder builder
    ) {

        String string = builder.getRemaining().toLowerCase(Locale.ROOT);
        BY_STRING.keySet().stream()
            .filter(candidate -> matchesSubStr(string, candidate.toLowerCase(Locale.ROOT)))
            .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
