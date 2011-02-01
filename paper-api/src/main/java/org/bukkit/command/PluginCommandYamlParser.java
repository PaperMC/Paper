package org.bukkit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.plugin.Plugin;

public class PluginCommandYamlParser {

    public static List<Command> parse(Plugin plugin) {
        List<Command> pluginCmds = new ArrayList<Command>();
        Object object = plugin.getDescription().getCommands();
        if (object == null)
            return pluginCmds;

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>)object;

        if (map != null) {
            for(Entry<String, Map<String, Object>> entry : map.entrySet()) {
                Command newCmd = new PluginCommand(entry.getKey(),plugin);
                Object description = entry.getValue().get("description");
                Object usage = entry.getValue().get("usage");
                Object aliases = entry.getValue().get("aliases");

                if (description != null)
                    newCmd.setTooltip(description.toString());

                if (usage != null) {
                    newCmd.setUsage(usage.toString());
                }

                if (aliases != null) {
                    List<String> aliasList = new ArrayList<String>();

                    for(String a : aliases.toString().split(",")) {
                            aliasList.add(a);
                    }

                    newCmd.setAliases(aliasList);
                }

                pluginCmds.add(newCmd);
            }
        }
        return pluginCmds;
    }

}
