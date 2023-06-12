package org.bukkit.craftbukkit.command;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.tree.CommandNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.entity.vehicle.EntityMinecartCommandBlock;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public final class VanillaCommandWrapper extends BukkitCommand {

    private final CommandDispatcher dispatcher;
    public final CommandNode<CommandListenerWrapper> vanillaCommand;

    public VanillaCommandWrapper(CommandDispatcher dispatcher, CommandNode<CommandListenerWrapper> vanillaCommand) {
        super(vanillaCommand.getName(), "A Mojang provided command.", vanillaCommand.getUsageText(), Collections.EMPTY_LIST);
        this.dispatcher = dispatcher;
        this.vanillaCommand = vanillaCommand;
        this.setPermission(getPermission(vanillaCommand));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        CommandListenerWrapper icommandlistener = getListener(sender);
        dispatcher.performPrefixedCommand(icommandlistener, toDispatcher(args, getName()), toDispatcher(args, commandLabel));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        Preconditions.checkArgument(sender != null, "Sender cannot be null");
        Preconditions.checkArgument(args != null, "Arguments cannot be null");
        Preconditions.checkArgument(alias != null, "Alias cannot be null");

        CommandListenerWrapper icommandlistener = getListener(sender);
        ParseResults<CommandListenerWrapper> parsed = dispatcher.getDispatcher().parse(toDispatcher(args, getName()), icommandlistener);

        List<String> results = new ArrayList<>();
        dispatcher.getDispatcher().getCompletionSuggestions(parsed).thenAccept((suggestions) -> {
            suggestions.getList().forEach((s) -> results.add(s.getText()));
        });

        return results;
    }

    public static CommandListenerWrapper getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle().createCommandSourceStack();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getWrapper();
        }
        if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlock().createCommandSourceStack();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer) MinecraftServer.getServer()).rconConsoleSource.createCommandSourceStack();
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer().createCommandSourceStack();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }

        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }

    public static String getPermission(CommandNode<CommandListenerWrapper> vanillaCommand) {
        return "minecraft.command." + ((vanillaCommand.getRedirect() == null) ? vanillaCommand.getName() : vanillaCommand.getRedirect().getName());
    }

    private String toDispatcher(String[] args, String name) {
        return name + ((args.length > 0) ? " " + Joiner.on(' ').join(args) : "");
    }
}
