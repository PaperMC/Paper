package io.papermc.paper.command;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpList;
import net.minecraft.server.players.ServerOpListEntry;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

public class PaperOPInfoCommand {
    public static final String DESCRIPTION = "View information about operators";

    public static LiteralCommandNode<CommandSourceStack> create() {
        final PaperOPInfoCommand command = new PaperOPInfoCommand();

        return Commands.literal("opinfo")
            .requires(source -> source.getSender().hasPermission("bukkit.command.opinfo"))
            .executes(command::executeList)
            .then(Commands.argument("player", ArgumentTypes.playerProfiles())
                .executes(command::executePlayer))
            .build();
    }

    private int executeList(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        MinecraftServer server = MinecraftServer.getServer();
        ServerOpList opList = server.getPlayerList().getOps();

        Collection<ServerOpListEntry> entries = opList.getEntries();
        if (entries.isEmpty()) {
            sender.sendMessage(text("There are no operators on this server.", NamedTextColor.YELLOW));
            return 0;
        }

        sender.sendMessage(text("--- Server Operators ---", NamedTextColor.BLUE));
        for (ServerOpListEntry entry : entries) {
            formatAndSendEntry(sender, entry);
        }

        return entries.size();
    }

    private int executePlayer(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        MinecraftServer server = MinecraftServer.getServer();
        ServerOpList opList = server.getPlayerList().getOps();

        try {
            PlayerProfileListResolver resolver = context.getArgument("player", PlayerProfileListResolver.class);
            Collection<PlayerProfile> profiles = resolver.resolve(context.getSource());

            if (profiles.isEmpty()) {
                sender.sendMessage(text("Player not found.", NamedTextColor.RED));
                return 0;
            }

            for (PlayerProfile profile : profiles) {
                UUID uuid = profile.getId();
                String name = profile.getName();

                if (uuid == null || name == null) {
                    continue;
                }

                NameAndId nameAndId = new NameAndId(uuid, name);
                ServerOpListEntry entry = opList.get(nameAndId);

                if (entry == null) {
                    sender.sendMessage(text(name + " is not an operator.", NamedTextColor.RED));
                } else {
                    formatAndSendEntry(sender, entry);
                }
            }
        } catch (Exception e) {
            sender.sendMessage(text("Error resolving target player.", NamedTextColor.RED));
            return 0;
        }

        return Command.SINGLE_SUCCESS;
    }

    private void formatAndSendEntry(CommandSender sender, ServerOpListEntry entry) {
        NameAndId user = entry.getUser();
        String name = (user != null) ? user.name() : "Unknown";

        PermissionLevel level = entry.permissions().level();
        boolean bypassesLimit = entry.getBypassesPlayerLimit();

        Component info = text()
            .append(text("- ", NamedTextColor.GRAY))
            .append(text(name, NamedTextColor.AQUA))
            .append(text(" | Level: ", NamedTextColor.WHITE))
            .append(text(String.valueOf(level), NamedTextColor.AQUA))
            .append(text(" | Bypasses Player Limit: ", NamedTextColor.WHITE))
            .append(text(bypassesLimit, bypassesLimit ? NamedTextColor.GREEN : NamedTextColor.RED))
            .build();

        sender.sendMessage(info);
    }
}
