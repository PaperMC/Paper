package io.papermc.paper.command.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static org.bukkit.command.Command.broadcastCommandMessage;

@NullMarked
public final class ReloadCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("reload")
            .requires(source -> source.getSender().hasPermission(PaperCommand.BASE_PERM + "reload"))
            .executes(context -> {
                doReload(context.getSource().getSender());
                return Command.SINGLE_SUCCESS;
            });
    }

    private static void doReload(final CommandSender sender) {
        broadcastCommandMessage(sender, text("Please note that this command is not supported and may cause issues.", RED));
        broadcastCommandMessage(sender, text("If you encounter any issues please use the /stop command to restart your server.", RED));

        MinecraftServer server = MinecraftServer.getServer();
        server.paperConfigurations.reloadConfigs(server);
        server.server.reloadCount++;

        broadcastCommandMessage(sender, text("Paper config reload complete.", GREEN));
    }
}
