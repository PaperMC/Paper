package com.destroystokyo.paper.exception;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Thrown when a command throws an exception
 */
public class ServerCommandException extends ServerException {

    private final Command command;
    private final CommandSender commandSender;
    private final String[] arguments;

    public ServerCommandException(String message, Throwable cause, Command command, CommandSender commandSender, String[] arguments) {
        super(message, cause);
        this.commandSender = checkNotNull(commandSender, "commandSender");
        this.arguments = checkNotNull(arguments, "arguments");
        this.command = checkNotNull(command, "command");
    }

    public ServerCommandException(Throwable cause, Command command, CommandSender commandSender, String[] arguments) {
        super(cause);
        this.commandSender = checkNotNull(commandSender, "commandSender");
        this.arguments = checkNotNull(arguments, "arguments");
        this.command = checkNotNull(command, "command");
    }

    protected ServerCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Command command, CommandSender commandSender, String[] arguments) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.commandSender = checkNotNull(commandSender, "commandSender");
        this.arguments = checkNotNull(arguments, "arguments");
        this.command = checkNotNull(command, "command");
    }

    /**
     * Gets the command which threw the exception
     *
     * @return exception throwing command
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Gets the command sender which executed the command request
     *
     * @return command sender of exception thrown command request
     */
    public CommandSender getCommandSender() {
        return commandSender;
    }

    /**
     * Gets the arguments which threw the exception for the command
     *
     * @return arguments of exception thrown command request
     */
    public String[] getArguments() {
        return arguments;
    }
}
