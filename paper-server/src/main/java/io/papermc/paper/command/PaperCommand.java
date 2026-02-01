package io.papermc.paper.command;

import com.mojang.brigadier.Command;
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
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minecraft.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;

@NullMarked
public final class PaperCommand {

    public static final DateTimeFormatter FILENAME_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT); // could use the formatter in Util too

    public static Component asFriendlyPath(Path path) {
        return text(path.toString())
            .hoverEvent(text("Click to copy the full path of the file"))
            .clickEvent(ClickEvent.copyToClipboard(path.toAbsolutePath().toString()));
    }

    public static final String BASE_PERM = "bukkit.command.paper.";
    static final String DESCRIPTION = "Paper related commands";

    public static LiteralCommandNode<CommandSourceStack> create() {
        LiteralArgumentBuilder<CommandSourceStack> rootNode = Commands.literal("paper")
            .requires(source -> source.getSender().hasPermission("bukkit.command.paper"))
            .executes(context -> {
                context.getSource().getSender().sendPlainMessage("/paper [" + String.join(" | ", SUBCOMMANDS.keySet()) + "]");
                return Command.SINGLE_SUCCESS;
            });

        SUBCOMMANDS.values().forEach(cmd -> cmd.forEach(rootNode::then));
        return rootNode.build();
    }

    public static void registerPermissions() {
        final List<String> permissions = new ArrayList<>();
        permissions.add("bukkit.command.paper");
        permissions.addAll(SUBCOMMANDS.keySet().stream().map(s -> BASE_PERM + s).toList());

        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        for (final String permission : permissions) {
            pluginManager.addPermission(new Permission(permission, PermissionDefault.OP));
        }
    }

    // subcommand label -> subcommand
    private static final Map<String, List<LiteralArgumentBuilder<CommandSourceStack>>> SUBCOMMANDS = Util.make(new HashMap<>(14), map -> {
        map.put("heap", List.of(HeapDumpCommand.create()));
        map.put("entity", List.of(EntityCommand.create()));
        map.put("reload", List.of(ReloadCommand.create()));
        map.put("version", List.of(
            VersionCommand.create(BASE_PERM + "version", "version"),
            VersionCommand.create(BASE_PERM + "version", "ver"))
        );
        map.put("dumpplugins", List.of(DumpPluginsCommand.create()));
        map.put("syncloadinfo", List.of(SyncLoadInfoCommand.create()));
        map.put("dumpitem", List.of(DumpItemCommand.create()));
        map.put("mobcaps", List.of(MobcapsCommand.createGlobal()));
        map.put("playermobcaps", List.of(MobcapsCommand.createPlayer()));
        map.put("dumplisteners", List.of(DumpListenersCommand.create()));
        FeatureHooks.registerPaperCommands(map);
    });
}
