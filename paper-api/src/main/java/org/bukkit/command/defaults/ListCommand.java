package org.bukkit.command.defaults;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

@Deprecated
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

        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        for (Player player : players) {
            // If a player is hidden from the sender don't show them in the list
            if (sender instanceof Player && !((Player) sender).canSee(player))
                continue;

            if (online.length() > 0) {
                online.append(", ");
            }

            online.append(player.getDisplayName());
        }

        sender.sendMessage("There are " + players.size() + "/" + Bukkit.getMaxPlayers() + " players online:\n" + online.toString());

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        return ImmutableList.of();
    }
}
