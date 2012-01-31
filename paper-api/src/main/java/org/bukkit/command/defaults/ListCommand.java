package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
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

        StringBuilder players = new StringBuilder();

        for (Player player : Bukkit.getOnlinePlayers()) {
            // If a player is hidden from the sender don't show them in the list
            if (sender instanceof Player && !((Player) sender).canSee(player))
                continue;

            if (players.length() > 0) {
                players.append(", ");
            }

            players.append(player.getDisplayName());
        }

        sender.sendMessage("Connected players: " + players.toString());

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("list");
    }
}
