package io.papermc.paper.brigadier;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.Component;

public final class TagParseCommandSyntaxException extends CommandSyntaxException {

    private static final SimpleCommandExceptionType EXCEPTION_TYPE = new SimpleCommandExceptionType(new LiteralMessage("Error parsing NBT"));

    public TagParseCommandSyntaxException(final String message) {
        super(EXCEPTION_TYPE, Component.literal(message));
    }
}
