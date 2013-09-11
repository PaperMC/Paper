package org.bukkit.command;

/**
 * Represents a command that delegates to one or more other commands
 */
public class MultipleCommandAlias extends Command {
    private Command[] commands;

    public MultipleCommandAlias(String name, Command[] commands) {
        super(name);
        this.commands = commands;
    }

    /**
     * Gets the commands associated with the multi-command alias.
     *
     * @return commands associated with alias
     */
    public Command[] getCommands() {
        return commands;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean result = false;

        for (Command command : commands) {
            result |= command.execute(sender, commandLabel, args);
        }

        return result;
    }
}
