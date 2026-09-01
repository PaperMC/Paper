package org.bukkit.command;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a command that delegates to one or more other commands
 *
 * @deprecated plugin developers should prefer to use the
 *     <a href="https://docs.papermc.io/paper/dev/command-api/basics/introduction/">Brigadier command API</a>
 */
@Deprecated(since = "26.3")
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
     */
    public @NotNull Command @NotNull [] getCommands() {
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
