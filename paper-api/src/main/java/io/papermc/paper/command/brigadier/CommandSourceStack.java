package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.tree.CommandNode;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.GameRule;
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
     */
    void sendToTarget(ComponentLike message);

    /**
     * Sends a system message to the {@link #getSender()}, admins, and console indicating successful command execution
     * according to vanilla semantics.
     *
     * <p>This currently includes checking for environments with suppressed output,
     * {@link GameRule#SEND_COMMAND_FEEDBACK}, and {@link GameRule#LOG_ADMIN_COMMANDS}.</p>
     *
     * @param message the message to send
     * @param allowInformingAdmins whether admins and console may be informed of this success
     */
    void sendSuccess(ComponentLike message, boolean allowInformingAdmins);

    /**
     * Sends a system message indicating a failed command execution to the {@link #getSender()}.
     * Does not apply red styling to the message as vanilla does to allow for custom failure message styling.
     *
     * <p>Respects vanilla semantics for accepting failure output and suppressed output environments.</p>
     *
     * @param message the message to send
     */
    void sendFailure(ComponentLike message);
}
