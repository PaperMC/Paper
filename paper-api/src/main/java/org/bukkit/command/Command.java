package org.bukkit.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public abstract class Command {
    private final String name;
    private List<String> aliases;
    protected String tooltip = "";
    protected String usageMessage;

    public Command(String name) {
        this.name = name;
        this.aliases = new ArrayList<String>();
        this.usageMessage = "/" + name;
    }

    public abstract boolean execute(Player player, String currentAlias, String[] args);
    
    public String getName() {
        return name;
    }
    
    public List<String> getAliases() {
        return aliases;
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getUsage() {
        return usageMessage;
    }
    
    public Command setAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }
    
    public Command setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }
    
    public Command setUsage(String usage) {
        this.usageMessage = usage;
        return this;
    }
}