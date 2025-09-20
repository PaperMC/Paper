package org.bukkit.craftbukkit.command;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.tree.CommandNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.minecart.CommandMinecart;

public class VanillaCommandWrapper extends BukkitCommand { // Paper

    public final CommandNode<CommandSourceStack> vanillaCommand;
    public final String helpCommandNamespace;

    public VanillaCommandWrapper(String name, String description, String usageMessage, List<String> aliases, CommandNode<CommandSourceStack> vanillaCommand, String helpCommandNamespace) {
        super(name, description, usageMessage, aliases);
        this.vanillaCommand = vanillaCommand;
        this.helpCommandNamespace = helpCommandNamespace;
    }

    Commands commands() {
        return net.minecraft.server.MinecraftServer.getServer().getCommands();
    }

    public VanillaCommandWrapper(CommandNode<CommandSourceStack> vanillaCommand) {
        super(vanillaCommand.getName(), "A Mojang provided command.", vanillaCommand.getUsageText(), Collections.emptyList());
        this.vanillaCommand = vanillaCommand;
        this.setPermission(VanillaCommandWrapper.getPermission(vanillaCommand));
        this.helpCommandNamespace = "Minecraft";
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) return true;

        CommandSourceStack source = VanillaCommandWrapper.getListener(sender);
        this.commands().performPrefixedCommand(source, this.toDispatcher(args, this.getName()));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        Preconditions.checkArgument(sender != null, "Sender cannot be null");
        Preconditions.checkArgument(args != null, "Arguments cannot be null");
        Preconditions.checkArgument(alias != null, "Alias cannot be null");

        CommandSourceStack source = VanillaCommandWrapper.getListener(sender);
        ParseResults<CommandSourceStack> parsed = this.commands().getDispatcher().parse(this.toDispatcher(args, this.getName()), source); // Paper

        List<String> results = new ArrayList<>();
        this.commands().getDispatcher().getCompletionSuggestions(parsed).thenAccept((suggestions) -> { // Paper
            suggestions.getList().forEach((s) -> results.add(s.getText()));
        });

        return results;
    }

    public static CommandSourceStack getListener(CommandSender sender) {
        if (sender instanceof CraftEntity entity) {
            if (sender instanceof CommandMinecart) {
                return ((CraftMinecartCommand) sender).getHandle().getCommandBlock().createCommandSourceStack(
                    ((CraftMinecartCommand) sender).getHandle().getCommandBlock().new CloseableCommandBlockSource()
                );
            }

            if (sender instanceof CraftPlayer player) {
                return player.getHandle().createCommandSourceStack();
            }

            return entity.getHandle().createCommandSourceStackForNameResolution((ServerLevel) entity.getHandle().level());
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getWrapper();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return ((CraftRemoteConsoleCommandSender) sender).getListener().createCommandSourceStack();
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer().createCommandSourceStack();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }
        if (sender instanceof io.papermc.paper.commands.FeedbackForwardingSender feedback) {
            return feedback.asVanilla();
        }

        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }

    public static String getPermission(CommandNode<CommandSourceStack> vanillaCommand) {
        // Paper start - Vanilla command permission fixes
        while (vanillaCommand.getRedirect() != null) {
            vanillaCommand = vanillaCommand.getRedirect();
        }
        final String commandName = vanillaCommand.getName();
        return "minecraft.command." + stripDefaultNamespace(commandName);
    }

    private static String stripDefaultNamespace(final String maybeNamespaced) {
        final String prefix = "minecraft:";
        if (maybeNamespaced.startsWith(prefix)) {
            return maybeNamespaced.substring(prefix.length());
        }
        return maybeNamespaced;
        // Paper end - Vanilla command permission fixes
    }

    private String toDispatcher(String[] args, String name) {
        return name + ((args.length > 0) ? " " + Joiner.on(' ').join(args) : "");
    }

    @Override
    public boolean canBeOverriden() {
        return true;
    }

    @Override
    public boolean isRegistered() {
        return true;
    }
}
