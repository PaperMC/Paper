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

        StringBuilder online = new StringBuilder();

        Player[] players = Bukkit.getOnlinePlayers();

        for (Player player : players) {
            // If a player is hidden from the sender don't show them in the list
            if (sender instanceof Player && !((Player) sender).canSee(player))
                continue;

            if (online.length() > 0) {
                online.append(", ");
            }

            online.append(player.getDisplayName());
        }

        sender.sendMessage("There are " + players.length + "/" + Bukkit.getMaxPlayers() + " players online:\n" + online.toString());

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("list");
    }
}
