package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeopCommand extends VanillaCommand {
    public DeopCommand() {
        super("deop");
        this.description = "Takes the specified player's operator status";
        this.usageMessage = "/deop <player>";
        this.setPermission("bukkit.command.op.take");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        Command.broadcastCommandMessage(sender, "De-opping " + args[0]);

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        player.setOp(false);

        if (player instanceof Player) {
            ((Player)player).sendMessage(ChatColor.YELLOW + "You are no longer op!");
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("deop ");
    }
}
