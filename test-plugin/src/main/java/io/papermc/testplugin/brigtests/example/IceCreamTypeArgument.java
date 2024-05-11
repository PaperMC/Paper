package io.papermc.testplugin.brigtests.example;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class IceCreamTypeArgument implements CustomArgumentType.Converted<IceCreamType, String> {

    @Override
    public @NotNull IceCreamType convert(String nativeType) throws CommandSyntaxException {
        try {
            return IceCreamType.valueOf(nativeType.toUpperCase());
        } catch (Exception e) {
            Message message = MessageComponentSerializer.message().serialize(Component.text("Invalid species %s!".formatted(nativeType), NamedTextColor.RED));

            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (IceCreamType species : IceCreamType.values()) {
            builder.suggest(species.name(), MessageComponentSerializer.message().serialize(Component.text("COOL! TOOLTIP!", NamedTextColor.GREEN)));
        }

        return CompletableFuture.completedFuture(
            builder.build()
        );
    }
}
