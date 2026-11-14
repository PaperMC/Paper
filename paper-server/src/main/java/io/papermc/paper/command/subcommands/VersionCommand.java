package io.papermc.paper.command.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.PaperVersionCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public final class VersionCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(String name) {
        return Commands.literal(name)
            .requires(PaperCommand.hasPermission("version"))
            .executes(PaperVersionCommand::serverVersion);
    }
}
