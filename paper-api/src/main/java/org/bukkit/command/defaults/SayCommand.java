package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SayCommand extends VanillaCommand {
    public SayCommand() {
        super("say");
        this.description = "Broadcasts the given message as the console";
        this.usageMessage = "/say <message>";
        this.setPermission("bukkit.command.say");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        Bukkit.getLogger().info("[" + sender.getName() + "] " + args[1]);
        Bukkit.broadcastMessage("[Server] " + args[1]);

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("say ");
    }
}
