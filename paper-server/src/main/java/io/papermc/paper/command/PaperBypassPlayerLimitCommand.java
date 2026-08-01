package io.papermc.paper.command;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpList;
import net.minecraft.server.players.ServerOpListEntry;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

@NullMarked
public class PaperBypassPlayerLimitCommand {
    public static final String DESCRIPTION = "Change bypass player limit privilege";

    public static LiteralCommandNode<CommandSourceStack> create() {
        final PaperBypassPlayerLimitCommand command = new PaperBypassPlayerLimitCommand();

        return Commands.literal("bypassplayerlimit")
            .requires(source -> source.getSender().hasPermission("bukkit.command.bypassplayerlimit"))
            .then(Commands.argument("player", ArgumentTypes.playerProfiles())
                .then(Commands.argument("bypass", BoolArgumentType.bool())
                    .executes(command::execute)))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        MinecraftServer server = MinecraftServer.getServer();
        ServerOpList opList = server.getPlayerList().getOps();

        boolean bypass = BoolArgumentType.getBool(context, "bypass");

        try {
            PlayerProfileListResolver resolver = context.getArgument("player", PlayerProfileListResolver.class);
            Collection<PlayerProfile> profiles = resolver.resolve(context.getSource());

            if (profiles.isEmpty()) {
                sender.sendMessage(text("Player not found.", NamedTextColor.RED));
                return 0;
            }

            int count = 0;
            for (PlayerProfile profile : profiles) {
                UUID uuid = profile.getId();
                String name = profile.getName();

                if (uuid == null || name == null) {
                    continue;
                }

                NameAndId nameAndId = new NameAndId(uuid, name);
                ServerOpListEntry existingEntry = opList.get(nameAndId);

                if (existingEntry == null) {
                    sender.sendMessage(text(name + " is not an operator.", NamedTextColor.RED));
                    continue;
                }

                ServerOpListEntry newEntry = new ServerOpListEntry(
                    nameAndId,
                    existingEntry.permissions(),
                    bypass
                );

                opList.add(newEntry);

                sender.sendMessage(text()
                    .append(text("Set bypass player limit privilege for ", NamedTextColor.WHITE))
                    .append(text(name, NamedTextColor.AQUA))
                    .append(text(" to ", NamedTextColor.WHITE))
                    .append(text(bypass, bypass ? NamedTextColor.GREEN : NamedTextColor.RED))
                    .append(text(".", NamedTextColor.WHITE))
                    .build()
                );
                count++;
            }
            return count;
        } catch (Exception e) {
            sender.sendMessage(text("Error executing command.", NamedTextColor.RED));
            return 0;
        }
    }
}
