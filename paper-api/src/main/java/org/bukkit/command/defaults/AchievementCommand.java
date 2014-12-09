package org.bukkit.command.defaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.Material;
import org.bukkit.Statistic.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import com.google.common.collect.ImmutableList;

@Deprecated
public class AchievementCommand extends VanillaCommand {
    public AchievementCommand() {
        super("achievement");
        this.description = "Gives the specified player an achievement or changes a statistic value. Use '*' to give all achievements.";
        this.usageMessage = "/achievement give <stat_name> [player]";
        this.setPermission("bukkit.command.achievement");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        if (!args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        String statisticString = args[1];
        Player player = null;

        if (args.length > 2) {
            player = Bukkit.getPlayer(args[1]);
        } else if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player == null) {
            sender.sendMessage("You must specify which player you wish to perform this action on.");
            return true;
        }

        if (statisticString.equals("*")) {
            for (Achievement achievement : Achievement.values()) {
                if (player.hasAchievement(achievement)) {
                    continue;
                }
                PlayerAchievementAwardedEvent event = new PlayerAchievementAwardedEvent(player, achievement);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    player.awardAchievement(achievement);
                }
            }
            Command.broadcastCommandMessage(sender, String.format("Successfully given all achievements to %s", player.getName()));
            return true;
        }

        Achievement achievement = Bukkit.getUnsafe().getAchievementFromInternalName(statisticString);
        Statistic statistic = Bukkit.getUnsafe().getStatisticFromInternalName(statisticString);

        if (achievement != null) {
            if (player.hasAchievement(achievement)) {
                sender.sendMessage(String.format("%s already has achievement %s", player.getName(), statisticString));
                return true;
            }

            PlayerAchievementAwardedEvent event = new PlayerAchievementAwardedEvent(player, achievement);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                sender.sendMessage(String.format("Unable to award %s the achievement %s", player.getName(), statisticString));
                return true;
            }
            player.awardAchievement(achievement);
                
            Command.broadcastCommandMessage(sender, String.format("Successfully given %s the stat %s", player.getName(), statisticString));
            return true;
        }

        if (statistic == null) {
            sender.sendMessage(String.format("Unknown achievement or statistic '%s'", statisticString));
            return true;
        }

        if (statistic.getType() == Type.UNTYPED) {
            PlayerStatisticIncrementEvent event = new PlayerStatisticIncrementEvent(player, statistic, player.getStatistic(statistic), player.getStatistic(statistic) + 1);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                sender.sendMessage(String.format("Unable to increment %s for %s", statisticString, player.getName()));
                return true;
            }
            player.incrementStatistic(statistic);
            Command.broadcastCommandMessage(sender, String.format("Successfully given %s the stat %s", player.getName(), statisticString));
            return true;
        }

        if (statistic.getType() == Type.ENTITY) {
            EntityType entityType = EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));

            if (entityType == null) {
                sender.sendMessage(String.format("Unknown achievement or statistic '%s'", statisticString));
                return true;
            }

            PlayerStatisticIncrementEvent event = new PlayerStatisticIncrementEvent(player, statistic, player.getStatistic(statistic), player.getStatistic(statistic) + 1, entityType);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                sender.sendMessage(String.format("Unable to increment %s for %s", statisticString, player.getName()));
                return true;
            }

            try {
                player.incrementStatistic(statistic, entityType);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(String.format("Unknown achievement or statistic '%s'", statisticString));
                return true;
            }
        } else {
            int id;
            try {
                id = getInteger(sender, statisticString.substring(statisticString.lastIndexOf(".") + 1), 0, Integer.MAX_VALUE, true);
            } catch (NumberFormatException e) {
                sender.sendMessage(e.getMessage());
                return true;
            }

            Material material = Material.getMaterial(id);

            if (material == null) {
                sender.sendMessage(String.format("Unknown achievement or statistic '%s'", statisticString));
                return true;
            }

            PlayerStatisticIncrementEvent event = new PlayerStatisticIncrementEvent(player, statistic, player.getStatistic(statistic), player.getStatistic(statistic) + 1, material);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                sender.sendMessage(String.format("Unable to increment %s for %s", statisticString, player.getName()));
                return true;
            }

            try {
                player.incrementStatistic(statistic, material);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(String.format("Unknown achievement or statistic '%s'", statisticString));
                return true;
            }
        }

        Command.broadcastCommandMessage(sender, String.format("Successfully given %s the stat %s", player.getName(), statisticString));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return Arrays.asList("give");
        }

        if (args.length == 2) {
            return Bukkit.getUnsafe().tabCompleteInternalStatisticOrAchievementName(args[1], new ArrayList<String>());
        }

        if (args.length == 3) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
