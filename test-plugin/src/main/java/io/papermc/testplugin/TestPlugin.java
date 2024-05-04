package io.papermc.testplugin;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.testplugin.example.ExampleAdminCommand;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // legacy registration via CommandMap
        this.registerLegacyCommands();

        // registration via lifecycle event system
        this.registerViaLifecycleEvents();
    }

    private void registerViaLifecycleEvents() {
        final LifecycleEventManager<Plugin> lifecycleManager = this.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
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
            commands.register(this.getPluginMeta(), Commands.literal("root_command")
                    .then(Commands.literal("sub_command")
                        .requires(source -> source.getSender().hasPermission("testplugin.test"))
                        .executes(ctx -> {
                            ctx.getSource().getSender().sendPlainMessage("root_command sub_command");
                            return Command.SINGLE_SUCCESS;
                        })).build(),
                null,
                Collections.emptyList()
            );

            commands.register(this.getPluginMeta(), "example", "test", Collections.emptyList(), new BasicCommand() {
                @Override
                public void execute(@NotNull final CommandSourceStack commandSourceStack, final @NotNull String[] args) {
                    System.out.println(Arrays.toString(args));
                }

                @Override
                public @NotNull Collection<String> suggest(final @NotNull CommandSourceStack commandSourceStack, final @NotNull String[] args) {
                    System.out.println(Arrays.toString(args));
                    return List.of("apple", "banana");
                }
            });


            commands.register(this.getPluginMeta(), Commands.literal("water")
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
                            return source.getExecutor().isInLava();
                        })
                        .executes(ctx -> {
                            ctx.getSource().getExecutor().sendMessage("You are in lava!");
                            return Command.SINGLE_SUCCESS;
                        })).build(),
                null,
                Collections.emptyList());


            ExampleAdminCommand.register(this, commands);
        }).priority(10));
    }

    private void registerLegacyCommands() {
        this.getServer().getCommandMap().register("fallback", new BukkitCommand("hi", "cool hi command", "<>", List.of("hialias")) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                sender.sendMessage("hi");
                return true;
            }
        });
        this.getServer().getCommandMap().register("fallback", new BukkitCommand("cooler-command", "cool hi command", "<>", List.of("cooler-command-alias")) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                sender.sendMessage("hi");
                return true;
            }
        });
        this.getServer().getCommandMap().getKnownCommands().values().removeIf((command) -> {
            return command.getName().equals("hi");
        });
    }
}
