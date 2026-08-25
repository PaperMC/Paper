package org.bukkit.event.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Thrown when a player executes a command that is not defined
 */
@NullMarked
public interface UnknownCommandEvent extends Event {

    /**
     * Gets the CommandSender
     *
     * @return sender of the command
     */
    CommandSender getSender();

    /**
     * Gets the command source associated with this event
     *
     * @return the {@link CommandSourceStack}
     */
    CommandSourceStack getCommandSource();

    /**
     * Gets the command that was sent
     *
     * @return command sent
     */
    String getCommandLine();

    /**
     * Gets the message that will be returned
     *
     * @return unknown command message
     * @deprecated use {@link #message()}
     */
    @Deprecated
    @Nullable String getMessage();

    /**
     * Sets the message that will be returned
     * <p>
     * Set to {@code null} to avoid any message being sent
     *
     * @param message the message to be returned, or {@code null}
     * @deprecated use {@link #message(Component)}
     */
    @Deprecated
    void setMessage(@Nullable String message);

    /**
     * Gets the message that will be returned
     *
     * @return unknown command message
     */
    @Contract(pure = true)
    @Nullable Component message();

    /**
     * Sets the message that will be returned
     * <p>
     * Set to {@code null} to avoid any message being sent
     *
     * @param message the message to be returned, or {@code null}
     */
    void message(@Nullable Component message);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

