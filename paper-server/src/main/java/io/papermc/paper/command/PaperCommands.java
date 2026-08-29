package io.papermc.paper.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandRegistrationFlag;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import org.bukkit.command.Command;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperCommands {

    private PaperCommands() {
    }

    public static void registerCommands() {
        registerInternalCommand(PaperCommand.create(), "paper", PaperCommand.DESCRIPTION, List.of(), Set.of());
        registerInternalCommand(PaperMSPTCommand.create(), "paper", PaperMSPTCommand.DESCRIPTION, List.of(), Set.of());
        registerInternalCommand(PaperVersionCommand.create(), "bukkit", PaperVersionCommand.DESCRIPTION, List.of("ver", "about"), Set.of());
        registerInternalCommand(PaperPluginsCommand.create(), "bukkit", PaperPluginsCommand.DESCRIPTION, List.of("pl"), Set.of());
    }

    private static void registerInternalCommand(final LiteralCommandNode<CommandSourceStack> node, final String namespace, final String description, final List<String> aliases, final Set<CommandRegistrationFlag> flags) {
        io.papermc.paper.command.brigadier.PaperCommands.INSTANCE.registerWithFlagsInternal(
            null,
            namespace,
            "Paper",
            node,
            description,
            aliases,
            flags
        );
    }
}
