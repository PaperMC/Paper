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
                Object d = entry.getValue().get("description");
                Object u = entry.getValue().get("usage");
                String description = "";
                String usageText = "";
                
                if (d != null)
                    description = d.toString();
                
                if (u != null)
                    usageText = u.toString();
    
                cmds.add(new Command(entry.getKey(), description, usageText, plugin));
            }
        }
        
        return cmds;
    }

}
