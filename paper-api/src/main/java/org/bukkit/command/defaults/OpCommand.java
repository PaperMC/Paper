package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class OpCommand extends VanillaCommand {
    public OpCommand() {
        super("op");
        this.description = "Gives the specified player operator status";
        this.usageMessage = "/op <player>";
        this.setPermission("bukkit.command.op.give");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1 || args[0].length() == 0)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        player.setOp(true);

        Command.broadcastCommandMessage(sender, "Opped " + args[0]);
        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("op");
    }
}
