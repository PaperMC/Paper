package org.bukkit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.plugin.Plugin;

public class PluginCommandYamlParser {

    public static List<Command> parse(Plugin plugin) {
        List<Command> pluginCmds = new ArrayList<Command>();

        Map<String, Map<String, Object>> map = plugin.getDescription().getCommands();

        if (map == null) {
            return pluginCmds;
        }

        for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
            Command newCmd = new PluginCommand(entry.getKey(), plugin);
            Object description = entry.getValue().get("description");
            Object usage = entry.getValue().get("usage");
            Object aliases = entry.getValue().get("aliases");
            Object permission = entry.getValue().get("permission");
            Object permissionMessage = entry.getValue().get("permission-message");

            if (description != null) {
                newCmd.setDescription(description.toString());
            }

            if (usage != null) {
                newCmd.setUsage(usage.toString());
            }

            if (aliases != null) {
                List<String> aliasList = new ArrayList<String>();

                if (aliases instanceof List) {
                    for (Object o : (List<?>) aliases) {
                        aliasList.add(o.toString());
                    }
                } else {
                    aliasList.add(aliases.toString());
                }

                newCmd.setAliases(aliasList);
            }

            if (permission != null) {
                newCmd.setPermission(permission.toString());
            }

            if (permissionMessage != null) {
                newCmd.setPermissionMessage(permissionMessage.toString());
            }

            pluginCmds.add(newCmd);
        }
        return pluginCmds;
    }
}
