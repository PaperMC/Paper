package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends VanillaCommand {
    public ListCommand() {
        super("list");
        this.description = "Lists all online players";
        this.usageMessage = "/list";
        this.setPermission("bukkit.command.list");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        String players = "";

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (players.length() > 0) {
                players += ", ";
            }

            players += player.getDisplayName();
        }

        sender.sendMessage("Connected players: " + players);

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("list");
    }
}
