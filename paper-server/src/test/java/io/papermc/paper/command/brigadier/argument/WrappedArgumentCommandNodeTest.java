package io.papermc.paper.command.brigadier.argument;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.minecraft.commands.arguments.SignedArgument;
import net.minecraft.network.chat.SignableCommand;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Normal
class WrappedArgumentCommandNodeTest {

    @Test
    void nativeParseKeepsSignedArgumentAfterCustomParseFailure() {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        ArgumentType<String> custom = reader -> {
            StringArgumentType.word().parse(reader);
            throw new SimpleCommandExceptionType(new LiteralMessage("unknown player")).create();
        };
        WrappedArgumentCommandNode<String, String> wrapped = new WrappedArgumentCommandNode<>(
            "player", custom, StringArgumentType.word(), null, source -> true, null, null, false, null
        );
        SignedArgument<String> signed = new SignedArgument<>() {
            @Override
            public String parse(final StringReader reader) throws CommandSyntaxException {
                return StringArgumentType.word().parse(reader);
            }
        };
        wrapped.addChild(new ArgumentCommandNode<>("message", signed, context -> 1, source -> true, null, null, false, null));
        dispatcher.getRoot().addChild(wrapped);
        CommandSourceStack source = Mockito.mock(CommandSourceStack.class);

        assertEquals(0, SignableCommand.of(dispatcher.parse("missing hello", source)).arguments().size());
        assertEquals("message", SignableCommand.of(WrappedArgumentCommandNode.parseNativeArguments(
            () -> dispatcher.parse("missing hello", source)
        )).arguments().getFirst().name());
        assertEquals(0, SignableCommand.of(dispatcher.parse("missing hello", source)).arguments().size());
    }
}
