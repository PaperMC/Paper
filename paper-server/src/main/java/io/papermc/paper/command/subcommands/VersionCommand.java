package io.papermc.paper.command.subcommands;

import io.papermc.paper.command.PaperSubcommand;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class VersionCommand implements PaperSubcommand {
    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        final @Nullable Command redirect = MinecraftServer.getServer().server.getCommandMap().getCommand("version");
        if (redirect != null) {
            redirect.execute(sender, "paper", new String[0]);
        }
        return true;
    }
}
