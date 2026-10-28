package io.papermc.paper.command.brigadier.exception;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;

public class SimpleCommandExceptionType implements CommandExceptionType {

    private final Message message;

    public SimpleCommandExceptionType(final Component message) {
        this.message = MessageComponentSerializer.message().serialize(message);
    }

    public CommandSyntaxException create() {
        return new CommandSyntaxException(this, this.message);
    }

    public CommandSyntaxException createWithContext(final ImmutableStringReader reader) {
        return new CommandSyntaxException(this, this.message, reader.getString(), reader.getCursor());
    }
}
