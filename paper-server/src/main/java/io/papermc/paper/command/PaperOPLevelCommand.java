package io.papermc.paper.command;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpList;
import net.minecraft.server.players.ServerOpListEntry;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

@NullMarked
public class PaperOPLevelCommand {
    public static final String DESCRIPTION = "Change operator permission levels";

    public static LiteralCommandNode<CommandSourceStack> create() {
        final PaperOPLevelCommand command = new PaperOPLevelCommand();

        return Commands.literal("oplevel")
            .requires(source -> source.getSender().hasPermission("bukkit.command.oplevel"))
            .then(Commands.argument("player", ArgumentTypes.playerProfiles())
                .then(Commands.argument("level", IntegerArgumentType.integer(0, 4))
                    .executes(command::execute)))
            .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        MinecraftServer server = MinecraftServer.getServer();
        ServerOpList opList = server.getPlayerList().getOps();

        int newLevel = IntegerArgumentType.getInteger(context, "level");

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

                boolean bypassesLimit = existingEntry != null && existingEntry.getBypassesPlayerLimit();

                PermissionLevel permLevel = PermissionLevel.byId(newLevel);
                ServerOpListEntry newEntry = new ServerOpListEntry(
                    nameAndId,
                    LevelBasedPermissionSet.forLevel(permLevel),
                    bypassesLimit
                );

                opList.add(newEntry);

                var serverPlayer = server.getPlayerList().getPlayer(uuid);
                if (serverPlayer != null) {
                    server.getPlayerList().sendPlayerPermissionLevel(serverPlayer);
                }

                sender.sendMessage(text()
                    .append(text("Set operator level for ", NamedTextColor.WHITE))
                    .append(text(name, NamedTextColor.AQUA))
                    .append(text(" to level ", NamedTextColor.WHITE))
                    .append(text(newLevel, NamedTextColor.AQUA))
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
