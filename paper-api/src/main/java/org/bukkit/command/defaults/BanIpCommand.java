package org.bukkit.command.defaults;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

@Deprecated
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

        String reason = args.length > 0 ? StringUtils.join(args, ' ', 1, args.length) : null;

        if (ipValidity.matcher(args[0]).matches()) {
            processIPBan(args[0], sender, reason);
        } else {
            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }

            processIPBan(player.getAddress().getAddress().getHostAddress(), sender, reason);
        }

        return true;
    }

    private void processIPBan(String ip, CommandSender sender, String reason) {
        Bukkit.getBanList(BanList.Type.IP).addBan(ip, reason, null, sender.getName());

        // Find all matching players and kick
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getAddress().getAddress().getHostAddress().equals(ip)) {
                player.kickPlayer("You have been IP banned.");
            }
        }

        Command.broadcastCommandMessage(sender, "Banned IP Address " + ip);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
