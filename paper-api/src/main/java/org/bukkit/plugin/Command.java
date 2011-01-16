package org.bukkit.plugin;

public final class Command {
    private final String name;
    private final String tooltip;
    private final String usage;
    private final Plugin owner;
    
    public Plugin getPlugin() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getHelpMessage() {
        return usage;
    }
    
    public Command(String name, String tooltip, String helpMessage, Plugin owner) {
        this.name = name;
        this.tooltip = tooltip;
        this.usage = helpMessage;
        this.owner = owner;
    }
}