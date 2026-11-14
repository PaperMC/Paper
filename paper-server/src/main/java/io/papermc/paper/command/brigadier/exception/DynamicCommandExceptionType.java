package io.papermc.paper.command.brigadier.exception;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.function.Function;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;

public class DynamicCommandExceptionType implements CommandExceptionType {

    private final Function<Object, Message> function;

    public DynamicCommandExceptionType(final Function<Object, Component> function) {
        this.function = arg -> MessageComponentSerializer.message().serialize(function.apply(arg));
    }

    public CommandSyntaxException create(final Object arg) {
        return new CommandSyntaxException(this, this.function.apply(arg));
    }

    public CommandSyntaxException createWithContext(final ImmutableStringReader reader, final Object arg) {
        return new CommandSyntaxException(this, this.function.apply(arg), reader.getString(), reader.getCursor());
    }
}
