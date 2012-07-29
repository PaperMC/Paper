package org.bukkit.command;

import java.util.List;

/**
 * Represents a class which can handle command tab completion and commands
 */
public interface TabCommandExecutor extends CommandExecutor {
    public List<String> onTabComplete();
}
