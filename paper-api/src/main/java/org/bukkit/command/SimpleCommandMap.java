package org.bukkit.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Server;

import org.bukkit.entity.Player;

public final class SimpleCommandMap implements CommandMap {
    private final Map<String, Command> knownCommands = new HashMap<String, Command>();

    public SimpleCommandMap(final Server server) {
        register("version", "bukkit", new Command("version") {
            @Override
            public boolean execute(Player player, String currentAlias, String[] args) {
                player.sendMessage("This server is using some funky dev build of Bukkit!");
                return true;
            }
        });

        register("reload", "bukkit", new Command("reload") {
            @Override
            public boolean execute(Player player, String currentAlias, String[] args) {
                if (player.isOp()) {
                    server.reload();
                    player.sendMessage(ChatColor.GREEN + "Reload complete.");
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have sufficient access"
                            + " to reload this server.");
                }

                return true;
            }
        });
    }

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
        boolean nameInUse = (knownCommands.get(name) != null);
        if (nameInUse)
            name = fallbackPrefix + ":" + name;
        
        knownCommands.put(name, command);
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
