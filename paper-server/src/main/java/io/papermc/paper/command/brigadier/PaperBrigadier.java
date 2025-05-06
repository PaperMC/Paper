package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.bukkit.BukkitCommandNode;
import java.util.Map;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;

public final class PaperBrigadier {

    @SuppressWarnings("DataFlowIssue")
    static final net.minecraft.commands.CommandSourceStack DUMMY = new net.minecraft.commands.CommandSourceStack(
        CommandSource.NULL,
        Vec3.ZERO,
        Vec2.ZERO,
        null,
        4,
        "",
        CommonComponents.EMPTY,
        null,
        null
    );

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Command wrapNode(CommandNode node) {
        if (!(node instanceof LiteralCommandNode)) {
            throw new IllegalArgumentException("Unsure how to wrap a " + node);
        }

        final APICommandMeta meta;
        if ((meta = node.apiCommandMeta) == null) {
            return new VanillaCommandWrapper(node);
        }
        CommandNode<CommandSourceStack> argumentCommandNode = node;
        if (argumentCommandNode.getRedirect() != null) {
            argumentCommandNode = argumentCommandNode.getRedirect();
        }

        Map<CommandNode<CommandSourceStack>, String> map = PaperCommands.INSTANCE.getDispatcherInternal().getSmartUsage(argumentCommandNode, DUMMY);
        String usage = map.isEmpty() ? node.getUsageText() :  node.getUsageText() + " " + String.join("\n" + node.getUsageText() + " ", map.values());

        // Internal command
        if (meta.pluginMeta() == null) {
            return new VanillaCommandWrapper(node.getName(), meta.description(), usage, meta.aliases(), node, meta.helpCommandNamespace());
        }

        return new PluginVanillaCommandWrapper(node.getName(), meta.description(), usage, meta.aliases(), node, meta.plugin());
    }

    /*
    Previously, Bukkit used one command dispatcher and ignored minecraft's reloading logic.

    In order to allow for legacy commands to be properly added, we will iterate through previous bukkit commands
    in the old dispatcher and re-register them.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void moveBukkitCommands(Commands before, Commands after) {
        CommandDispatcher erasedDispatcher = before.getDispatcher();

        for (Object node : erasedDispatcher.getRoot().getChildren()) {
            if (node instanceof CommandNode<?> commandNode && commandNode.getCommand() instanceof BukkitCommandNode.BukkitBrigCommand) {
                after.getDispatcher().getRoot().removeCommand(((CommandNode<?>) node).getName()); // Remove already existing commands
                after.getDispatcher().getRoot().addChild((CommandNode<net.minecraft.commands.CommandSourceStack>) node);
            }
        }
    }

    public static <S> LiteralCommandNode<S> copyLiteral(final String newLiteral, final LiteralCommandNode<S> source) {
        // logic copied from LiteralCommandNode#createBuilder
        final LiteralArgumentBuilder<S> copyBuilder = LiteralArgumentBuilder.<S>literal(newLiteral)
            .requires(source.getRequirement())
            .forward(source.getRedirect(), source.getRedirectModifier(), source.isFork());
        if (source.getCommand() != null) {
            copyBuilder.executes(source.getCommand());
        }

        for (final CommandNode<S> child : source.getChildren()) {
            copyBuilder.then(child);
        }
        return copyBuilder.build();
    }
}
