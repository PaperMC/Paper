package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Deprecated
public class MeCommand extends VanillaCommand {
    public MeCommand() {
        super("me");
        this.description = "Performs the specified action in chat";
        this.usageMessage = "/me <action>";
        this.setPermission("bukkit.command.me");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        StringBuilder message = new StringBuilder();
        message.append(sender.getName());

        for (String arg : args) {
            message.append(" ");
            message.append(arg);
        }

        Bukkit.broadcastMessage("* " + message.toString());

        return true;
    }
}
