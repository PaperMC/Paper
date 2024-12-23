package org.bukkit.command;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a command that delegates to one or more other commands
 *
 * @since 1.0.0
 */
public class MultipleCommandAlias extends Command {
    private Command[] commands;

    public MultipleCommandAlias(@NotNull String name, @NotNull Command[] commands) {
        super(name);
        this.commands = commands;
    }

    /**
     * Gets the commands associated with the multi-command alias.
     *
     * @return commands associated with alias
     * @since 1.1.0
     */
    @NotNull
    public Command[] getCommands() {
        return commands;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        boolean result = false;

        for (Command command : commands) {
            result |= command.execute(sender, commandLabel, args);
        }

        return result;
    }
}
