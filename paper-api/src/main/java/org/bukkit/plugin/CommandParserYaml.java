package org.bukkit.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CommandParserYaml {

    public static List<Command> parse(Plugin plugin) {
        List<Command> cmds = new ArrayList<Command>();
        Object object = plugin.getDescription().getCommands();
        
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>)object;
        
        if (map != null) {
            for(Entry<String, Map<String, Object>> entry : map.entrySet()) {
                String description = entry.getValue().get("description").toString();
                String usageText = entry.getValue().get("usage").toString();
    
                cmds.add(new Command(entry.getKey(), description, usageText, plugin));
            }
        }
        
        return cmds;
    }

}
