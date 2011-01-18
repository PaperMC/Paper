package org.bukkit.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public final class SimpleCommandMap implements CommandMap {
    private final Map<String, Command> knownCommands = new HashMap<String, Command>();

    /**
     * Registers multiple commands. Returns name of first command for which fallback had to be used if any.
     * @param plugin
     * @return
     */
    public void registerAll(String fallbackPrefix, List<Command> commands) {
        if (commands != null) {
            for(Command c : commands) {
                List<String> names = new ArrayList<String>();
                names.add(c.getName());
                names.addAll(c.getAliases());
                
                for(String name : names) {
                    register(name, fallbackPrefix, c);                        
                }
            }
        }
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean register(String name, String fallbackPrefix, Command command) {
        boolean nameInUse = (knownCommands.get(command.getName()) != null);
        if (nameInUse)
            name = fallbackPrefix + ":" + name;
        
        knownCommands.put(name, command);
        System.out.println("Adding cmd: " + name + " for plugin " + fallbackPrefix);
        
        return !nameInUse;
    }

    /** 
     * {@inheritDoc}
     */
    public boolean dispatch(Player sender, String commandLine) {
        String[] args = commandLine.split(" ");
        String sentCommandLabel = args[0].substring(1);
        
        Command target = knownCommands.get(sentCommandLabel);
        boolean isRegisteredCommand = (target != null);
        if (isRegisteredCommand) {
            target.execute(sender, sentCommandLabel, args);
        }
        return isRegisteredCommand;
    }

}
