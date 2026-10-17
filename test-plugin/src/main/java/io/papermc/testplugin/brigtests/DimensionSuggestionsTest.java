package io.papermc.testplugin.brigtests;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

public final class DimensionSuggestionsTest {

    private static final String WORLD_NAME = "dimension_suggestions_test";

    private DimensionSuggestionsTest() {
    }

    public static void register(JavaPlugin plugin) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> event.registrar().register(
            Commands.literal("dimension-suggestions")
                .requires(source -> source.getSender().isOp())
                .then(Commands.literal("create").executes(context -> {
                    World world = new WorldCreator(WORLD_NAME)
                        .type(WorldType.FLAT)
                        .generateStructures(false)
                        .createWorld();
                    if (world == null) {
                        context.getSource().getSender().sendPlainMessage("World creation was cancelled.");
                        return 0;
                    }
                    context.getSource().getSender().sendPlainMessage("Loaded world: " + world.getKey());
                    return Command.SINGLE_SUCCESS;
                }))
                .then(Commands.literal("unload").executes(context -> {
                    boolean unloaded = Bukkit.unloadWorld(WORLD_NAME, true);
                    context.getSource().getSender().sendPlainMessage("World unloaded: " + unloaded);
                    return unloaded ? Command.SINGLE_SUCCESS : 0;
                }))
                .then(Commands.literal("world").then(Commands.argument("dimension", ArgumentTypes.world()).executes(context -> {
                    World world = context.getArgument("dimension", World.class);
                    context.getSource().getSender().sendPlainMessage("Selected world: " + world.getKey());
                    return Command.SINGLE_SUCCESS;
                })))
                .build()
        ));
    }
}
