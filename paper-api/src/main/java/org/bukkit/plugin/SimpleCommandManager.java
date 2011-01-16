package org.bukkit.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public final class SimpleCommandManager implements CommandManager {
    private final Map<String, Command> registeredCommands = new HashMap<String, Command>();
    
    /**
     * Registers all the commands specified in the description file of a plugin.
     * @param plugin
     * @return
     */
    public boolean registerCommands(Plugin plugin) {
        List<Command> commands = CommandParserYaml.parse(plugin);
        boolean existsCommands = (commands != null);
        
        if (existsCommands) {
            for(Command c : commands) {
                if (!registerCommand(c))
                    return false; // Command name conflict :(
            }
        }
        return existsCommands;
    }
    
    public boolean registerCommand(Command command) {
        return registerCommand(command.getName(), command.getTooltip(), command.getHelpMessage(), command.getPlugin());
    }

    /** 
     * {@inheritDoc}
     */
    public boolean registerCommand(String command, String tooltip, String helpMessage, Plugin plugin) {
        boolean nameAvailable = (registeredCommands.get(command) == null);
        
        if (nameAvailable) {
            Command newCmd = new Command(command, tooltip, helpMessage, plugin);
            registeredCommands.put(command, newCmd);
        }        
        return nameAvailable;
    }

    /** 
     * {@inheritDoc}
     */
    public boolean dispatchCommand(Player sender, String cmdLine) {
        String[] args = cmdLine.split(" ");
       
        // Remove '/'-prefix and check if command is registered.
        Command target = registeredCommands.get(args[0].substring(1)); 
        boolean targetFound = (target != null);
        
        if (targetFound) {
            target.getPlugin().onCommand(sender, args[0], args);
        }
        return targetFound;
    }
}
