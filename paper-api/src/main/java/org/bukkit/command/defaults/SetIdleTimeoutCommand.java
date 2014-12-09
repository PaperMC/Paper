package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

@Deprecated
public class SetIdleTimeoutCommand extends VanillaCommand {

    public SetIdleTimeoutCommand() {
        super("setidletimeout");
        this.description = "Sets the server's idle timeout";
        this.usageMessage = "/setidletimeout <Minutes until kick>";
        this.setPermission("bukkit.command.setidletimeout");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length == 1) {
            int minutes;

            try {
                minutes = getInteger(sender, args[0], 0, Integer.MAX_VALUE, true);
            } catch (NumberFormatException ex) {
                sender.sendMessage(ex.getMessage());
                return true;
            }

            Bukkit.getServer().setIdleTimeout(minutes);

            Command.broadcastCommandMessage(sender, "Successfully set the idle timeout to " + minutes + " minutes.");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        return ImmutableList.of();
    }
}
