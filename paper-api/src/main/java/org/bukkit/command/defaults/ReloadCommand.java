package org.bukkit.command.defaults;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class ReloadCommand extends BukkitCommand {
    public ReloadCommand(@NotNull String name) {
        super(name);
        this.description = "Reloads the server configuration and plugins";
        this.usageMessage = "/reload [permissions|commands|confirm]"; // Paper
        this.setPermission("bukkit.command.reload");
        this.setAliases(Arrays.asList("rl"));
    }

    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    public static final String RELOADING_DISABLED_MESSAGE = "A lifecycle event handler has been registered which makes reloading plugins not possible"; // Paper

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String currentAlias, @NotNull String[] args) { // Paper
        if (!testPermission(sender)) return true;

        boolean confirmed = System.getProperty("LetMeReload") != null;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("permissions")) {
                Bukkit.getServer().reloadPermissions();
                Command.broadcastCommandMessage(sender, text("Permissions successfully reloaded.", net.kyori.adventure.text.format.NamedTextColor.GREEN));
                return true;
            } else if ("commands".equalsIgnoreCase(args[0])) {
                if (Bukkit.getServer().reloadCommandAliases()) {
                    Command.broadcastCommandMessage(sender, text("Command aliases successfully reloaded.", net.kyori.adventure.text.format.NamedTextColor.GREEN));
                } else {
                    Command.broadcastCommandMessage(sender, text("An error occurred while trying to reload command aliases.", net.kyori.adventure.text.format.NamedTextColor.RED));
                }
                return true;
            } else if ("confirm".equalsIgnoreCase(args[0])) {
                confirmed = true;
            } else {
                Command.broadcastCommandMessage(sender, text("Usage: " + usageMessage, net.kyori.adventure.text.format.NamedTextColor.RED));
                return true;
            }
        }
        if (!confirmed) {
            Command.broadcastCommandMessage(sender, text("Are you sure you wish to reload your server? This command will be removed soon. Doing so may cause bugs and memory leaks. It is recommended to restart instead of using /bukkit:reload. To confirm, please type ", net.kyori.adventure.text.format.NamedTextColor.RED).append(text("/bukkit:reload confirm", net.kyori.adventure.text.format.NamedTextColor.YELLOW)));
            return true;
        }

        Command.broadcastCommandMessage(sender, ChatColor.RED + "Please note that this command is not supported, may cause issues when using some plugins, and will be removed soon.");
        Command.broadcastCommandMessage(sender, ChatColor.RED + "If you encounter any issues please use the /stop command to restart your server.");
        // Paper start - lifecycle events
        try {
            Bukkit.reload();
        } catch (final IllegalStateException ex) {
            if (ex.getMessage().equals(RELOADING_DISABLED_MESSAGE)) {
                Command.broadcastCommandMessage(sender, ChatColor.RED + RELOADING_DISABLED_MESSAGE);
                return true;
            }
        }
        // Paper end - lifecycle events
        Command.broadcastCommandMessage(sender, ChatColor.GREEN + "Reload complete.");

        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return com.google.common.collect.Lists.newArrayList("permissions", "commands"); // Paper
    }
}
