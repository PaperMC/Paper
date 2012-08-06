package org.bukkit.command.defaults;

import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanIpCommand extends VanillaCommand {
    public static final Pattern ipValidity = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public BanIpCommand() {
        super("ban-ip");
        this.description = "Prevents the specified IP address from using this server";
        this.usageMessage = "/ban-ip <address|player> [reason ...]";
        this.setPermission("bukkit.command.ban.ip");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        // TODO: Ban Reason support
        if (ipValidity.matcher(args[0]).matches()) {
            processIPBan(args[0], sender);
        } else {
            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }

            processIPBan(player.getAddress().getAddress().getHostAddress(), sender);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return args.length >= 1 ? null : EMPTY_LIST;
    }

    private void processIPBan(String ip, CommandSender sender) {
        // TODO: Kick on ban
        Bukkit.banIP(ip);

        Command.broadcastCommandMessage(sender, "Banned IP Address " + ip);
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("ban-ip");
    }
}
