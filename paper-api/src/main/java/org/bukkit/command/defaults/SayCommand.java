package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

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
        if (args.length == 0)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        String message = "";

        for (int i = 0; i < args.length; i++) {
            if (i > 0) message += " ";
            message += args[i];
        }

        if (!(sender instanceof ConsoleCommandSender)) {
            Bukkit.getLogger().info("[" + sender.getName() + "] " + message);
        }

        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "[Server] " + message);

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("say ") || input.equalsIgnoreCase("say");
    }
}
