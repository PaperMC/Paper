package io.papermc.paper.command.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.minecraft.server.MinecraftServer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class VersionCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(String name) {
        return Commands.literal(name)
            .requires(PaperCommand.hasPermission("version"))
            .executes(context -> {
                final org.bukkit.command.Command redirect = MinecraftServer.getServer().server.getCommandMap().getCommand("version");
                if (redirect != null && redirect.execute(context.getSource().getSender(), "paper", new String[0])) {
                    return Command.SINGLE_SUCCESS;
                }

                return 0;
            });
    }
}
