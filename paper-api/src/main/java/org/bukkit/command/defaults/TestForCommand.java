package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Deprecated
public class TestForCommand extends VanillaCommand {
    public TestForCommand() {
        super("testfor");
        this.description = "Tests whether a specifed player is online";
        this.usageMessage = "/testfor <player>";
        this.setPermission("bukkit.command.testfor");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        sender.sendMessage(ChatColor.RED + "/testfor is only usable by commandblocks with analog output.");
        return true;
    }
}
