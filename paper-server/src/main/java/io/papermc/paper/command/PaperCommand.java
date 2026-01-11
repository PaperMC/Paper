package io.papermc.paper.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.FeatureHooks;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.subcommands.DumpItemCommand;
import io.papermc.paper.command.subcommands.DumpListenersCommand;
import io.papermc.paper.command.subcommands.DumpPluginsCommand;
import io.papermc.paper.command.subcommands.EntityCommand;
import io.papermc.paper.command.subcommands.HeapDumpCommand;
import io.papermc.paper.command.subcommands.MobcapsCommand;
import io.papermc.paper.command.subcommands.ReloadCommand;
import io.papermc.paper.command.subcommands.SyncLoadInfoCommand;
import io.papermc.paper.command.subcommands.VersionCommand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperCommand {

    public static final String BASE_PERM = "bukkit.command.paper.";
    static final String DESCRIPTION = "Paper related commands";

    public static LiteralCommandNode<CommandSourceStack> create() {
        // TODO: FIX ISSUES WITH COMMANDS
        // TODO: TEST COMMANDS

        final List<String> permissions = new ArrayList<>();
        permissions.add("bukkit.command.paper");
        permissions.addAll(SUBCOMMANDS.keySet().stream().map(s -> BASE_PERM + s).toList());

        LiteralArgumentBuilder<CommandSourceStack> rootNode = Commands.literal("paper")
            .requires(stack -> stack.getSender().hasPermission(String.join(";", permissions)))
            .executes(ctx -> {
                ctx.getSource().getSender().sendPlainMessage("/paper [" + String.join(" | ", SUBCOMMANDS.keySet()) + "]");
                return com.mojang.brigadier.Command.SINGLE_SUCCESS;
            });


        SUBCOMMANDS.values().forEach(subs -> subs.forEach(rootNode::then));
        return rootNode.build();
    }

    public static void registerPermissions() {
        final List<String> permissions = new ArrayList<>();
        permissions.add("bukkit.command.paper");
        permissions.addAll(SUBCOMMANDS.keySet().stream().map(s -> BASE_PERM + s).toList());

        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        for (final String perm : permissions) {
            pluginManager.addPermission(new Permission(perm, PermissionDefault.OP));
        }
    }

    // subcommand label -> subcommand
    private static final Map<String, List<LiteralArgumentBuilder<CommandSourceStack>>> SUBCOMMANDS = Util.make(() -> {
        final Map<String, List<LiteralArgumentBuilder<CommandSourceStack>>> commands = new HashMap<>(14);

        commands.put("heap", List.of(HeapDumpCommand.create()));
        commands.put("entity", List.of(EntityCommand.create()));
        commands.put("reload", List.of(ReloadCommand.create()));
        commands.put("version", List.of(
            VersionCommand.create(BASE_PERM + "version", "version"),
            VersionCommand.create(BASE_PERM + "version", "ver"))
        );
        commands.put("dumpplugins", List.of(DumpPluginsCommand.create()));
        commands.put("syncloadinfo", List.of(SyncLoadInfoCommand.create()));
        commands.put("dumpitem", List.of(DumpItemCommand.create()));
        commands.put("mobcaps", List.of(MobcapsCommand.createGlobal()));
        commands.put("playermobcaps", List.of(MobcapsCommand.createPlayer()));
        commands.put("dumplisteners", List.of(DumpListenersCommand.create()));
        FeatureHooks.registerPaperCommands(commands);

        return commands;
    });
}
