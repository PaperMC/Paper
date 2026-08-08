package io.papermc.paper.event.function;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a command is run by a function. It is
 * called early in the command handling process.
 * <p>
 * Many plugins will have <b>no use for this event</b>, and you should
 * attempt to avoid using it if it is not necessary.
 * <p>
 * Some examples of valid uses for this event are:
 * <ul>
 * <li>Logging executed commands to a separate file</li>
 * <li>Conditionally blocking commands executed from functions.</li>
 * <li>Conditionally modify commands executed from functions.</li>
 * </ul>
 * <p>
 * Examples of incorrect uses are:
 * <ul>
 * <li>Using this event to run command logic
 * </ul>
 * <p>
 * If the event is cancelled, processing of the command will halt.
 * <p>
 * The command here is without (<code>/</code>) at the
 * beginning as the minecraft function expected. So
 * if you set the command, it should also be without (<code>/</code>)
 */
public class FunctionCommandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final CommandSender sender;
    private String command;

    private boolean cancelled;

    @ApiStatus.Internal
    public FunctionCommandEvent(@NotNull final CommandSender sender, @NotNull final String command) {
        this.sender = sender;
        this.command = command;
    }

    /**
     * Get the command sender.
     *
     * @return The sender
     */
    @NotNull
    public CommandSender getSender() {
        return this.sender;
    }

    /**
     * Gets the command that the function is attempting to execute.
     *
     * @return Command the function is attempting to execute
     */
    @NotNull
    public String getCommand() {
        return this.command;
    }

    /**
     * Sets the command that the function is attempting to execute.
     *
     * @param command New command that the function will execute
     */
    public void setCommand(@NotNull final String command) {
        this.command = command;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
