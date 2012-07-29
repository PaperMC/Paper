package org.bukkit.command.defaults;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
        if (args.length != 1 || args[0].length() == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        player.setOp(false);

        if (player instanceof Player) {
            ((Player) player).sendMessage(ChatColor.YELLOW + "You are no longer op!");
        }

        sender.sendMessage("De-opped " + args[0]);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return args.length >= 1 ? null : EMPTY_LIST;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("deop");
    }
}
