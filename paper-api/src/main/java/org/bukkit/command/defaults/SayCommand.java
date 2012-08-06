package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        StringBuilder message = new StringBuilder();
        if (args.length > 0) {
            message.append(args[0]);
            for (int i = 1; i < args.length; i++) {
                message.append(" ");
                message.append(args[i]);
            }
        }

        if (sender instanceof Player) {
            Bukkit.getLogger().info("[" + sender.getName() + "] " + message);
        }

        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "[Server] " + message);

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("say");
    }
}
