package com.destroystokyo.paper.exception;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Called when a tab-complete request throws an exception
 */
public class ServerTabCompleteException extends ServerCommandException {

    public ServerTabCompleteException(String message, Throwable cause, Command command, CommandSender commandSender, String[] arguments) {
        super(message, cause, command, commandSender, arguments);
    }

    public ServerTabCompleteException(Throwable cause, Command command, CommandSender commandSender, String[] arguments) {
        super(cause, command, commandSender, arguments);
    }

    protected ServerTabCompleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Command command, CommandSender commandSender, String[] arguments) {
        super(message, cause, enableSuppression, writableStackTrace, command, commandSender, arguments);
    }
}
