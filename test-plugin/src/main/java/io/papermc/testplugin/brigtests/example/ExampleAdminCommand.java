package io.papermc.testplugin.brigtests.example;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.testplugin.TestPlugin;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExampleAdminCommand {

    public static void register(JavaPlugin plugin, Commands commands) {
        final LiteralArgumentBuilder<CommandSourceStack> adminBuilder = Commands.literal("admin")
            .executes((ct) -> {
                ct.getSource().getSender().sendPlainMessage("root admin");
                return 1;
            })
            .then(
                Commands.literal("tp")
                    .then(
                        Commands.argument("player", ArgumentTypes.player()).executes((source) -> {
                            CommandSourceStack sourceStack = source.getSource();
                            Player resolved = source.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(sourceStack).get(0);

                            if (resolved == source.getSource().getExecutor()) {
                                source.getSource().getExecutor().sendMessage(Component.text("Can't teleport to self!"));
                                return 0;
                            }
                            Entity entity = source.getSource().getExecutor();
                            if (entity != null) {
                                entity.teleport(resolved);
                            }

                            return 1;
                        })
                    )
            )
            .then(
                Commands.literal("tp-self")
                    .executes((cmd) -> {
                        if (cmd.getSource().getSender() instanceof Player player) {
                            player.teleport(cmd.getSource().getLocation());
                        }

                        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                    })
            )
            .then(
                Commands.literal("broadcast")
                    .then(
                        Commands.argument("message", ArgumentTypes.component()).executes((source) -> {
                            Component message = source.getArgument("message", Component.class);
                            Bukkit.broadcast(message);
                            return 1;
                        })
                    )
            )
            .then(
                Commands.literal("ice_cream").then(
                    Commands.argument("type", new IceCreamTypeArgument()).executes((context) -> {
                        IceCreamType argumentResponse = context.getArgument("type", IceCreamType.class); // Gets the raw argument
                        context.getSource().getSender().sendMessage(Component.text("You like: " + argumentResponse));
                        return 1;
                    })
                )
            )
            .then(
                Commands.literal("execute")
                    .redirect(commands.getDispatcher().getRoot().getChild("execute"))
            )
            .then(
                Commands.literal("signed_message").then(
                    Commands.argument("msg", ArgumentTypes.signedMessage()).executes((context) -> {
                        SignedMessageResolver argumentResponse = context.getArgument("msg", SignedMessageResolver.class); // Gets the raw argument

                        // This is a better way of getting signed messages, includes the concept of "disguised" messages.
                        argumentResponse.resolveSignedMessage("msg", context)
                            .thenAccept((signedMsg) -> {
                                context.getSource().getSender().sendMessage(signedMsg, ChatType.SAY_COMMAND.bind(Component.text("STATIC")));
                            });

                        return 1;
                    })
                )
            )
            .then(
                Commands.literal("setblock").then(
                    Commands.argument("block", ArgumentTypes.blockState())
                        .then(Commands.argument("pos", ArgumentTypes.blockPosition())
                            .executes((context) -> {
                                CommandSourceStack sourceStack = context.getSource();
                                BlockPosition position = context.getArgument("pos", BlockPositionResolver.class).resolve(sourceStack);
                                BlockState state = context.getArgument("block", BlockState.class);

                                // TODO: better block state api here? :thinking:
                                Block block = context.getSource().getLocation().getWorld().getBlockAt(position.blockX(), position.blockY(), position.blockZ());
                                block.setType(state.getType());
                                block.setBlockData(state.getBlockData());

                                return 1;
                            })
                        )
                )
            );
        commands.register(plugin.getPluginMeta(), adminBuilder.build(), "Cool command showcasing what you can do!", List.of("alias_for_admin_that_you_shouldnt_use", "a"));


        Bukkit.getCommandMap().register(
            "legacy",
            new Command("legacy_command") {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                    return List.of(String.join(" ", args));
                }
            }
        );

        Bukkit.getCommandMap().register(
            "legacy",
            new Command("legacy_fail") {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                    return false;
                }

                @Override
                public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                    return List.of(String.join(" ", args));
                }
            }
        );
    }
}
