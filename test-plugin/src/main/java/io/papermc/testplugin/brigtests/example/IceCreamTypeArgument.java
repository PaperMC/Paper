package io.papermc.testplugin.brigtests.example;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class IceCreamTypeArgument implements CustomArgumentType.Converted<IceCreamType, String> {

    @Override
    public IceCreamType convert(String nativeType) throws CommandSyntaxException {
        try {
            return IceCreamType.valueOf(nativeType.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException _) {
            throw new ComponentCommandExceptionType(Component.text("Invalid species %s!".formatted(nativeType), NamedTextColor.RED)).create();
        }
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (IceCreamType species : IceCreamType.values()) {
            builder.suggest(species.name(), MessageComponentSerializer.message().serialize(Component.text("COOL! TOOLTIP!", NamedTextColor.GREEN)));
        }

        return builder.buildFuture();
    }
}
