package io.papermc.testplugin.brigtests;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.RegistryArgumentExtractor;
import io.papermc.paper.command.brigadier.argument.range.DoubleRangeProvider;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.testplugin.brigtests.example.ExampleAdminCommand;
import io.papermc.testplugin.brigtests.example.MaterialArgumentType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Registration {
    
    private Registration() {
    }
    
    public static void registerViaOnEnable(final JavaPlugin plugin) {
        registerLegacyCommands(plugin);
        registerViaLifecycleEvents(plugin);
    }

    private static void registerViaLifecycleEvents(final JavaPlugin plugin) {
        final LifecycleEventManager<Plugin> lifecycleManager = plugin.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(Commands.literal("ench")
                .then(
                    Commands.argument("name", ArgumentTypes.resource(RegistryKey.ENCHANTMENT))
                        .executes(ctx -> {
                            ctx.getSource().getSender().sendPlainMessage(ctx.getArgument("name", Enchantment.class).toString());
                            return Command.SINGLE_SUCCESS;
                        })
                ).build()
            );
            commands.register(Commands.literal("ench-key")
                .then(
                    Commands.argument("key", ArgumentTypes.resourceKey(RegistryKey.ENCHANTMENT))
                        .executes(ctx -> {
                            final TypedKey<Enchantment> key = RegistryArgumentExtractor.getTypedKey(ctx, RegistryKey.ENCHANTMENT, "key");
                            ctx.getSource().getSender().sendPlainMessage(key.toString());
                            return Command.SINGLE_SUCCESS;
                        })
                ).build()
            );
            commands.register(Commands.literal("fine-pos")
                  .then(
                      Commands.argument("pos", ArgumentTypes.finePosition(false))
                          .executes(ctx -> {
                              final FinePositionResolver position = ctx.getArgument("pos", FinePositionResolver.class);
                              ctx.getSource().getSender().sendPlainMessage("Position: " + position.resolve(ctx.getSource()));
                              return Command.SINGLE_SUCCESS;
                          })
                  ).build()
            );
            // ensure plugin commands override
            commands.register(Commands.literal("tag")
                    .executes(ctx -> {
                        ctx.getSource().getSender().sendPlainMessage("overriden command");
                        return Command.SINGLE_SUCCESS;
                    })
                    .build(),
                null,
                Collections.emptyList()
            );
        });

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            final Commands commands = event.registrar();
            commands.register(plugin.getPluginMeta(), Commands.literal("root_command")
                    .then(Commands.literal("sub_command")
                        .requires(source -> source.getSender().hasPermission("testplugin.test"))
                        .executes(ctx -> {
                            ctx.getSource().getSender().sendPlainMessage("root_command sub_command");
                            return Command.SINGLE_SUCCESS;
                        })).build(),
                null,
                Collections.emptyList()
            );

            commands.register(plugin.getPluginMeta(), "example", "test", Collections.emptyList(), new BasicCommand() {
                @Override
                public void execute(@NotNull final CommandSourceStack commandSourceStack, final @NotNull String @NotNull [] args) {
                    System.out.println(Arrays.toString(args));
                }

                @Override
                public @NotNull Collection<String> suggest(final @NotNull CommandSourceStack commandSourceStack, final @NotNull String @NotNull [] args) {
                    System.out.println(Arrays.toString(args));
                    return List.of("apple", "banana");
                }
            });


            commands.register(plugin.getPluginMeta(), Commands.literal("water")
                    .requires(source -> {
                        System.out.println("isInWater check");
                        return source.getExecutor().isInWater();
                    })
                    .executes(ctx -> {
                        ctx.getSource().getExecutor().sendMessage("You are in water!");
                        return Command.SINGLE_SUCCESS;
                    }).then(Commands.literal("lava")
                        .requires(source -> {
                            System.out.println("isInLava check");
                            if (source.getExecutor() != null) {
                                return source.getExecutor().isInLava();
                            }
                            return true;
                        })
                        .executes(ctx -> {
                            ctx.getSource().getExecutor().sendMessage("You are in lava!");
                            return Command.SINGLE_SUCCESS;
                        })).build(),
                null,
                Collections.emptyList());


            ExampleAdminCommand.register(plugin, commands);
        }).priority(10));
    }

    private static void registerLegacyCommands(final JavaPlugin plugin) {
        plugin.getServer().getCommandMap().register("fallback", new BukkitCommand("hi", "cool hi command", "<>", List.of("hialias")) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                sender.sendMessage("hi");
                return true;
            }
        });
        plugin.getServer().getCommandMap().register("fallback", new BukkitCommand("cooler-command", "cool hi command", "<>", List.of("cooler-command-alias")) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                sender.sendMessage("hi");
                return true;
            }
        });
        plugin.getServer().getCommandMap().getKnownCommands().values().removeIf((command) -> {
            return command.getName().equals("hi");
        });
    }

    public static void registerViaBootstrap(final BootstrapContext context) {
        final LifecycleEventManager<BootstrapContext> lifecycleManager = context.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(Commands.literal("material")
                    .then(Commands.literal("item")
                        .then(Commands.argument("mat", MaterialArgumentType.item())
                            .executes(ctx -> {
                                ctx.getSource().getSender().sendPlainMessage(ctx.getArgument("mat", Material.class).name());
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    ).then(Commands.literal("block")
                        .then(Commands.argument("mat", MaterialArgumentType.block())
                            .executes(ctx -> {
                                ctx.getSource().getSender().sendPlainMessage(ctx.getArgument("mat", Material.class).name());
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                    .build(),
                null,
                Collections.emptyList()
            );
        });

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            final Commands commands = event.registrar();
            commands.register(Commands.literal("heya")
                    .then(Commands.argument("range", ArgumentTypes.doubleRange())
                        .executes((ct) -> {
                            ct.getSource().getSender().sendPlainMessage(ct.getArgument("range", DoubleRangeProvider.class).range().toString());
                            return 1;
                        })
                    ).build(),
                null,
                Collections.emptyList()
            );
        }).priority(10));
    }
}
