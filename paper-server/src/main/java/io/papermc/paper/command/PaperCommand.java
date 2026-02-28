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
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

    public static Component asFriendlyPath(final Path path) {
        return text(path.toString())
            .hoverEvent(text("Click to copy the full path of the file"))
            .clickEvent(ClickEvent.copyToClipboard(path.toAbsolutePath().toString()));
    }

    public static final String BASE_PERM = "bukkit.command.paper";
    static final String DESCRIPTION = "Paper related commands";

    public static LiteralCommandNode<CommandSourceStack> create() {
        final LiteralArgumentBuilder<CommandSourceStack> rootNode = Commands.literal("paper")
            .requires(source -> source.getSender().hasPermission(BASE_PERM) || SUBPERMISSIONS.stream().anyMatch(name -> source.getSender().hasPermission(name)))
            .executes(context -> {
                context.getSource().getSender().sendPlainMessage("/paper [" + SUBCOMMANDS.keySet().stream().filter(
                    name -> hasPermission(name).test(context.getSource())
                ).collect(Collectors.joining(" | ")) + "]");
                return Command.SINGLE_SUCCESS;
            });

        SUBCOMMANDS.values().forEach(rootNode::then);
        rootNode.then(VersionCommand.create("ver"));

        return rootNode.build();
    }

    public static Predicate<CommandSourceStack> hasPermission(String name) {
        return source -> source.getSender().hasPermission(BASE_PERM) || source.getSender().hasPermission(BASE_PERM + '.' + name);
    }

    public static void registerPermissions() {
        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.addPermission(new Permission(BASE_PERM, PermissionDefault.OP));
        for (final String permission : SUBPERMISSIONS) {
            pluginManager.addPermission(new Permission(permission, PermissionDefault.OP));
        }
    }

    private static final Map<String, LiteralArgumentBuilder<CommandSourceStack>> SUBCOMMANDS = Util.make(new ObjectArrayList<LiteralArgumentBuilder<CommandSourceStack>>(), list -> {
        list.add(HeapDumpCommand.create());
        list.add(EntityCommand.create());
        list.add(ReloadCommand.create());
        list.add(VersionCommand.create("version"));
        list.add(DumpPluginsCommand.create());
        list.add(SyncLoadInfoCommand.create());
        list.add(DumpItemCommand.create());
        list.add(MobcapsCommand.createGlobal());
        list.add(MobcapsCommand.createPlayer());
        list.add(DumpListenersCommand.create());
        FeatureHooks.registerPaperCommands(list);
    }).stream().collect(Collectors.toMap(LiteralArgumentBuilder::getLiteral, Function.identity()));

    private static final List<String> SUBPERMISSIONS = SUBCOMMANDS.keySet().stream().map(name -> BASE_PERM + '.' + name).toList();
}
