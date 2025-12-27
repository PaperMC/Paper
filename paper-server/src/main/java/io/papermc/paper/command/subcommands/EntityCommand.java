package io.papermc.paper.command.subcommands;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.HeightMap;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@NullMarked
public final class EntityCommand {

    private static final Component HELP_MESSAGE = text("Use /paper entity [list] help for more information on a specific command", RED);
    private static final Component LIST_HELP_MESSAGE = text("Use /paper entity list [filter] [worldName] to get entity info that matches the optional filter.", RED);

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("entity")
            .requires(stack -> stack.getSender().hasPermission(PaperCommand.BASE_PERM + "entity"))

            .then(Commands.literal("list")
                .then(Commands.literal("help")
                    .executes(ctx -> {
                        ctx.getSource().getSender().sendMessage(LIST_HELP_MESSAGE);
                        return Command.SINGLE_SUCCESS;
                    })
                )

                .then(Commands.argument("filter", StringArgumentType.string())
                    .suggests((ctx, builder) -> {
                        BuiltInRegistries.ENTITY_TYPE.keySet().stream()
                            .map(Functions.toStringFunction())
                            .filter(type -> type.startsWith(builder.getRemainingLowerCase()))
                            .forEach(builder::suggest);
                        return builder.buildFuture();
                    })

                    .executes(ctx -> {
                        listEntities(
                            ctx.getSource().getSender(),
                            StringArgumentType.getString(ctx, "filter"),
                            ctx.getSource().getLocation().getWorld()
                        );
                        return Command.SINGLE_SUCCESS;
                    })

                    .then(Commands.argument("world", ArgumentTypes.world())
                        .executes(ctx -> {
                            listEntities(
                                ctx.getSource().getSender(),
                                StringArgumentType.getString(ctx, "filter"),
                                ctx.getArgument("world", World.class)
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )

                .executes(ctx -> {
                    listEntities(ctx.getSource().getSender(), "*", ctx.getSource().getLocation().getWorld());
                    return Command.SINGLE_SUCCESS;
                })
            )

            .then(Commands.literal("help")
                .executes(ctx -> {
                    ctx.getSource().getSender().sendMessage(HELP_MESSAGE);
                    return Command.SINGLE_SUCCESS;
                })
            )

            .executes(ctx -> {
                ctx.getSource().getSender().sendMessage(HELP_MESSAGE);
                return Command.SINGLE_SUCCESS;
            });
    }

    /*
     * Ported from MinecraftForge - author: LexManos <LexManos@gmail.com> - License: LGPLv2.1
     */
    private static void listEntities(final CommandSender sender, final String filter, final World bukkitWorld) {
        final String cleanfilter = filter.replace("?", ".?").replace("*", ".*?");
        Set<Identifier> names = BuiltInRegistries.ENTITY_TYPE.keySet().stream()
            .filter(n -> n.toString().matches(cleanfilter))
            .collect(Collectors.toSet());
        if (names.isEmpty()) {
            sender.sendMessage(text("Invalid filter, does not match any entities. Use /paper entity list for a proper list", RED));
            sender.sendMessage(text("Usage: /paper entity list [filter] [worldName]", RED));
            return;
        }

        Map<Identifier, MutablePair<Integer, Map<ChunkPos, Integer>>> list = Maps.newHashMap();
        ServerLevel world = ((CraftWorld) bukkitWorld).getHandle();
        Map<Identifier, Integer> nonEntityTicking = Maps.newHashMap();
        ServerChunkCache chunkProviderServer = world.getChunkSource();
        world.getAllEntities().forEach(e -> {
            Identifier key = EntityType.getKey(e.getType());

            MutablePair<Integer, Map<ChunkPos, Integer>> info = list.computeIfAbsent(key, k -> MutablePair.of(0, Maps.newHashMap()));
            ChunkPos chunk = e.chunkPosition();
            info.left++;
            info.right.put(chunk, info.right.getOrDefault(chunk, 0) + 1);
            if (!world.isPositionEntityTicking(e.blockPosition()) || (e instanceof net.minecraft.world.entity.Marker && !world.paperConfig().entities.markers.tick)) { // Paper - Configurable marker ticking
                nonEntityTicking.merge(key, 1, Integer::sum);
            }
        });
        if (names.size() == 1) {
            Identifier name = names.iterator().next();
            Pair<Integer, Map<ChunkPos, Integer>> info = list.get(name);
            int nonTicking = nonEntityTicking.getOrDefault(name, 0);
            if (info == null) {
                sender.sendMessage(text("No entities found.", RED));
                return;
            }
            sender.sendMessage("Entity: " + name + " Total Ticking: " + (info.getLeft() - nonTicking) + ", Total Non-Ticking: " + nonTicking);
            info.getRight().entrySet().stream()
                .sorted((a, b) -> !a.getValue().equals(b.getValue()) ? b.getValue() - a.getValue() : a.getKey().toString().compareTo(b.getKey().toString()))
                .limit(10).forEach(e -> {
                    final int x = (e.getKey().x << 4) + 8;
                    final int z = (e.getKey().z << 4) + 8;
                    final Component message = text("  " + e.getValue() + ": " + e.getKey().x + ", " + e.getKey().z + (chunkProviderServer.isPositionTicking(e.getKey().toLong()) ? " (Ticking)" : " (Non-Ticking)"))
                        .hoverEvent(HoverEvent.showText(text("Click to teleport to chunk", GREEN)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/minecraft:execute as @s in " + world.getWorld().getKey() + " run tp " + x + " " + (world.getWorld().getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING) + 1) + " " + z));
                    sender.sendMessage(message);
                });
        } else {
            List<Pair<Identifier, Integer>> info = list.entrySet().stream()
                .filter(e -> names.contains(e.getKey()))
                .map(e -> Pair.of(e.getKey(), e.getValue().left))
                .sorted((a, b) -> !a.getRight().equals(b.getRight()) ? b.getRight() - a.getRight() : a.getKey().toString().compareTo(b.getKey().toString()))
                .toList();

            if (info.isEmpty()) {
                sender.sendMessage(text("No entities found.", RED));
                return;
            }

            int count = info.stream().mapToInt(Pair::getRight).sum();
            int nonTickingCount = nonEntityTicking.values().stream().mapToInt(Integer::intValue).sum();
            sender.sendMessage("Total Ticking: " + (count - nonTickingCount) + ", Total Non-Ticking: " + nonTickingCount);
            info.forEach(e -> {
                int nonTicking = nonEntityTicking.getOrDefault(e.getKey(), 0);
                sender.sendMessage("  " + (e.getValue() - nonTicking) + " (" + nonTicking + ") " + ": " + e.getKey());
            });
            sender.sendMessage("* First number is ticking entities, second number is non-ticking entities");
        }
    }
}
