package org.bukkit.command;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public final class PluginCommand extends Command {
    private final Plugin owningPlugin;

    public PluginCommand(String name, Plugin owner) {
        super(name);
        this.owningPlugin = owner;
        this.usageMessage = "";
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean cmdSuccess = false;
        
        try {
            cmdSuccess = owningPlugin.onCommand(sender, this, commandLabel, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + owningPlugin.getDescription().getFullName(), ex);
        }

        if (!cmdSuccess && !usageMessage.isEmpty()) {
            String tmpMsg = usageMessage.replace("<command>", commandLabel);
            String[] usageLines = tmpMsg.split("\\n");
            for(String line: usageLines) {
                while (line.length() > 0) {
                    int stripChars = (line.length() > 53 ? 53:line.length());
                    sender.sendMessage(ChatColor.RED + line.substring(0, stripChars));
                    line = line.substring(stripChars);
                }
            }
        }
        return cmdSuccess;
    }
}