package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;

public class GameModeCommand extends VanillaCommand {
    public GameModeCommand() {
        super("gamemode");
        this.description = "Changes the player to a specific game mode";
        this.usageMessage = "/gamemode <player> <gamemode>";
        this.setPermission("bukkit.command.gamemode");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        Player player = Bukkit.getPlayerExact(args[0]);

        if (player != null) {
            int value = -1;

            try {
                value = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {}

            GameMode mode = GameMode.getByValue(value);

            if (mode != null) {
                if (mode != player.getGameMode()) {
                    Command.broadcastCommandMessage(sender, "Setting " + player.getName() + " to game mode " + mode.getValue());

                    player.setGameMode(mode);
                } else {
                    sender.sendMessage(player.getName() + " already has game mode" + mode.getValue());
                }
            } else {
                sender.sendMessage("There is no game mode with id " + args[1]);
            }
        } else {
            sender.sendMessage("Can't find user " + args[0]);
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("gamemode ") || input.equalsIgnoreCase("gamemode");
    }
}
