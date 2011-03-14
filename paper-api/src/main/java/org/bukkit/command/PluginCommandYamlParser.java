package org.bukkit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.plugin.Plugin;

public class PluginCommandYamlParser {

    @SuppressWarnings("unchecked")
    public static List<Command> parse(Plugin plugin) {
        List<Command> pluginCmds = new ArrayList<Command>();
        Object object = plugin.getDescription().getCommands();
        if (object == null)
            return pluginCmds;

        Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>)object;

        if (map != null) {
            for(Entry<String, Map<String, Object>> entry : map.entrySet()) {
                Command newCmd = new PluginCommand(entry.getKey(),plugin);
                Object description = entry.getValue().get("description");
                Object usage = entry.getValue().get("usage");
                Object aliases = entry.getValue().get("aliases");

                if (description != null)
                    newCmd.setDescription(description.toString());

                if (usage != null) {
                    newCmd.setUsage(usage.toString());
                }

                if (aliases != null) {
                    List<String> aliasList = new ArrayList<String>();
                    
                    if (aliases instanceof List) {
                        for (Object o : (List<Object>)aliases) {
                            aliasList.add(o.toString());
                        }
                    } else {
                        aliasList.add(aliases.toString());
                    }

                    newCmd.setAliases(aliasList);
                }

                pluginCmds.add(newCmd);
            }
        }
        return pluginCmds;
    }

}
