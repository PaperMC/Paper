package io.papermc.paper.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@DefaultQualifier(NonNull.class)
public final class InvSeeCommand extends Command {

    public InvSeeCommand(final String name) {
        super(name);
        this.description = "Open another player's inventory";
        this.usageMessage = "/invsee <player>";
        this.setPermission("bukkit.command.invsee");
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!testPermission(sender)) return true;

        if (!(sender instanceof final Player viewer)) {
            sender.sendMessage(text("Only players can use this command.", RED));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(text("Usage: " + this.usageMessage, RED));
            return true;
        }

        final Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(text("Player not found or not online: " + args[0], RED));
            return true;
        }

        if (target.equals(viewer)) {
            sender.sendMessage(text("You cannot open your own inventory with this command.", RED));
            return true;
        }

        viewer.openInventory(target.getInventory());
        sender.sendMessage(
            text("Opened ", GRAY)
                .append(text(target.getName(), GOLD))
                .append(text("'s inventory.", GRAY))
        );
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args, final Location location) {
        if (args.length == 1) {
            final List<String> names = new ArrayList<>();
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (sender instanceof final Player p) {
                    if (p.equals(player)) continue;
                    if (!p.canSee(player)) continue;
                }
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    names.add(player.getName());
                }
            }
            return names;
        }
        return Collections.emptyList();
    }
}
