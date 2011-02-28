package org.bukkit.command;

import org.bukkit.plugin.Plugin;

public final class PluginCommand extends Command {
    private final Plugin owningPlugin;
    private CommandExecutor executor;

    protected PluginCommand(String name, Plugin owner) {
        super(name);
        this.executor = owner;
        this.owningPlugin = owner;
        this.usageMessage = "";
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean success = false;

        try {
            success = executor.onCommand(sender, this, commandLabel, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + owningPlugin.getDescription().getFullName(), ex);
        }

        if (!success && !usageMessage.isEmpty()) {
            sender.sendMessage(usageMessage.replace("<command>", commandLabel));
        }
        
        return success;
    }

    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }
}