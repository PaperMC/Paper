package io.papermc.paper.command.brigadier;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.GameRules;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * The command source type for Brigadier commands registered using Paper API.
 * <p>
 * While the general use case for CommandSourceStack is similar to that of {@link CommandSender}, it provides access to
 * important additional context for the command execution.
 * Specifically, commands such as {@literal /execute} may alter the location or executor of the source stack before
 * passing it to another command.
 * <p>The {@link CommandSender} returned by {@link #getSender()} may be a "no-op"
 * instance of {@link CommandSender} in cases where the server either doesn't
 * exist yet, or no specific sender is available. Methods on such a {@link CommandSender}
 * will either have no effect or throw an {@link UnsupportedOperationException}.</p>
 */
@ApiStatus.NonExtendable
public interface CommandSourceStack {

    /**
     * Gets the location that this command is being executed at.
     *
     * @return a cloned location instance.
     */
    Location getLocation();

    /**
     * Gets the command sender that executed this command.
     * The sender of a command source stack is the one that initiated/triggered the execution of a command.
     * It differs to {@link #getExecutor()} as the executor can be changed by a command, e.g. {@literal /execute}.
     *
     * @return the command sender instance
     */
    CommandSender getSender();

    /**
     * Gets the entity that executes this command.
     * May not always be {@link #getSender()} as the executor of a command can be changed to a different entity
     * than the one that triggered the command.
     *
     * @return entity that executes this command
     */
    @Nullable Entity getExecutor();

    /**
     * {@return the {@link Player} that is executing this command}
     * @throws CommandSyntaxException if the {@link #getExecutor() executor} of this command is not a {@link Player}
     */
    Player getPlayerOrThrow() throws CommandSyntaxException;

    /**
     * {@return the {@link Entity} that is executing this command}
     * @throws CommandSyntaxException if the {@link #getExecutor() executor} of this command is not an {@link Entity}
     */
    Entity getEntityOrThrow() throws CommandSyntaxException;

    /**
     * Creates a new CommandSourceStack object with a different location for redirecting commands to other nodes.
     *
     * @param location The location to create a new CommandSourceStack object with
     * @return The newly created CommandSourceStack
     * @see #getLocation()
     * @see com.mojang.brigadier.builder.ArgumentBuilder#fork(CommandNode, RedirectModifier)
     */
    CommandSourceStack withLocation(Location location);

    /**
     * Creates a new CommandSourceStack object with a different executor for redirecting commands to other nodes.
     *
     * @param executor The executing entity to create a new CommandSourceStack object with
     * @return The newly created CommandSourceStack
     * @see #getExecutor()
     * @see com.mojang.brigadier.builder.ArgumentBuilder#fork(CommandNode, RedirectModifier)
     */
    CommandSourceStack withExecutor(Entity executor);

    /**
     * Sends a system message to the {@link #getExecutor()} if it is a {@link Player},
     * otherwise sends a system message to the {@link #getSender()}.
     *
     * @param message the message to send
     * @throws IllegalArgumentException if the message is null
     */
    void sendReply(ComponentLike message);

    /**
     * Sends a system message with the MiniMessage format to the {@link #getExecutor()}
     * if it is a {@link Player}, otherwise sends it to the {@link #getSender()}.
     *
     * <p>See <a href="https://docs.papermc.io/adventure/minimessage/">MiniMessage docs</a> and
     * <a href="https://docs.papermc.io/adventure/minimessage/dynamic-replacements/">MiniMessage Placeholders docs</a>
     * for more information on the format.</p>
     *
     * @param message the MiniMessage message to send
     * @param resolvers resolvers to use
     * @throws IllegalArgumentException if the message is null
     */
    default void sendRichReply(String message, TagResolver... resolvers) {
        Preconditions.checkArgument(message != null, "message cannot be null.");
        this.sendReply(MiniMessage.miniMessage().deserialize(
            message,
            getExecutor() instanceof Player player ? player : getSender(),
            resolvers
        ));
    }

    /**
     * Sends a system message to the {@link #getSender()}, admins, and console indicating successful command execution
     * according to vanilla semantics.
     *
     * <p>This currently includes checking for environments with suppressed output,
     * {@link GameRules#SEND_COMMAND_FEEDBACK}, and {@link GameRules#LOG_ADMIN_COMMANDS}.</p>
     *
     * @param message the message to send
     * @param allowInformingAdmins whether admins and console may be informed of this success
     * @throws IllegalArgumentException if the message is null
     */
    void sendSuccess(ComponentLike message, boolean allowInformingAdmins);

    /**
     * Sends a system message to the {@link #getSender()}, admins, and console indicating successful command execution
     * according to vanilla semantics. This method informs admins and the console of this success.
     *
     * <p>This currently includes checking for environments with suppressed output,
     * {@link GameRules#SEND_COMMAND_FEEDBACK}, and {@link GameRules#LOG_ADMIN_COMMANDS}.</p>
     *
     * @param message the message to send
     * @see #sendSuccess(ComponentLike, boolean) to disable if admins and console should be informed
     * @throws IllegalArgumentException if the message is null
     */
    default void sendSuccess(ComponentLike message) {
        this.sendSuccess(message, true);
    }

    /**
     * Sends a system message with the MiniMessage format to the {@link #getSender()}, admins, and console indicating
     * successful command execution according to vanilla semantics.
     *
     * <p>This currently includes checking for environments with suppressed output,
     * {@link GameRules#SEND_COMMAND_FEEDBACK}, and {@link GameRules#LOG_ADMIN_COMMANDS}.</p>
     *
     * <p>See <a href="https://docs.papermc.io/adventure/minimessage/">MiniMessage docs</a> and
     * <a href="https://docs.papermc.io/adventure/minimessage/dynamic-replacements/">MiniMessage Placeholders docs</a>
     * for more information on the format.</p>
     *
     * @param message the MiniMessage message to send
     * @param allowInformingAdmins whether admins and console may be informed of this success
     * @param resolvers resolvers to use
     * @throws IllegalArgumentException if the message is null
     */
    default void sendRichSuccess(String message, boolean allowInformingAdmins, TagResolver... resolvers) {
        Preconditions.checkArgument(message != null, "message cannot be null.");
        this.sendSuccess(MiniMessage.miniMessage().deserialize(message, getSender(), resolvers), allowInformingAdmins);
    }

    /**
     * Sends a system message with the MiniMessage format to the {@link #getSender()}, admins, and console indicating
     * successful command execution according to vanilla semantics. This method informs admins and the console of
     * this success.
     *
     * <p>This currently includes checking for environments with suppressed output,
     * {@link GameRules#SEND_COMMAND_FEEDBACK}, and {@link GameRules#LOG_ADMIN_COMMANDS}.</p>
     *
     * <p>See <a href="https://docs.papermc.io/adventure/minimessage/">MiniMessage docs</a> and
     * <a href="https://docs.papermc.io/adventure/minimessage/dynamic-replacements/">MiniMessage Placeholders docs</a>
     * for more information on the format.</p>
     *
     * @param message the MiniMessage message to send
     * @param resolvers resolvers to use
     * @see #sendSuccess(ComponentLike, boolean) to disable if admins and console should be informed
     * @throws IllegalArgumentException if the message is null
     */
    default void sendRichSuccess(String message, TagResolver... resolvers) {
        this.sendRichSuccess(message, true, resolvers);
    }

    /**
     * Sends a system message indicating a failed command execution to the {@link #getSender()}.
     * Does not apply red styling to the message as vanilla does to allow for custom failure message styling.
     *
     * <p>Respects vanilla semantics for accepting failure output and suppressed output environments.</p>
     *
     * @param message the message to send
     * @throws IllegalArgumentException if the message is null
     */
    void sendFailure(ComponentLike message);

    /**
     * Sends a system message with the MiniMessage format indicating a failed command execution to the{@link #getSender()}.
     * Does not apply red styling to the message as vanilla does to allow for custom failure message styling.
     *
     * <p>Respects vanilla semantics for accepting failure output and suppressed output environments.</p>
     *
     * <p>See <a href="https://docs.papermc.io/adventure/minimessage/">MiniMessage docs</a> and
     * <a href="https://docs.papermc.io/adventure/minimessage/dynamic-replacements/">MiniMessage Placeholders docs</a>
     * for more information on the format.</p>
     *
     * @param message the MiniMessage message to send
     * @param resolvers resolvers to use
     * @throws IllegalArgumentException if the message is null
     */
    default void sendRichFailure(String message, TagResolver... resolvers) {
        Preconditions.checkArgument(message != null, "message cannot be null.");
        this.sendFailure(MiniMessage.miniMessage().deserialize(message, getSender(), resolvers));
    }
}
