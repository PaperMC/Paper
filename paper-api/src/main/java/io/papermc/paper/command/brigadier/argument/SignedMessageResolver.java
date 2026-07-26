package io.papermc.paper.command.brigadier.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.chat.SignedMessage;
import org.jetbrains.annotations.ApiStatus;

/**
 * A resolver for a {@link SignedMessage}
 *
 * @see ArgumentTypes#signedMessage()
 */
@ApiStatus.NonExtendable
public interface SignedMessageResolver {

    /**
     * Gets the string content of the message
     *
     * @return string content
     */
    String content();

    /**
     * Resolves this signed message. This will the {@link CommandContext}
     * and signed arguments sent by the client.
     * <p>
     * In the case that signed message information isn't provided, a "system"
     * signed message will be returned instead.
     *
     * @param argumentName argument name
     * @param context the command context
     * @return a completable future for the {@link SignedMessage}
     * @throws CommandSyntaxException syntax exception
     */
    CompletableFuture<SignedMessage> resolveSignedMessage(String argumentName, CommandContext<CommandSourceStack> context) throws CommandSyntaxException;
}
