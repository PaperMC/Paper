package io.papermc.paper.command.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.VanillaArgumentProviderImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public final class EntityCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("entity")
            .requires(PaperCommand.hasPermission("entity"))
            .then(Commands.literal("list")
                .executes(context -> {
                    return listEntities(context.getSource().getSender(), BuiltInRegistries.ENTITY_TYPE.listElements().toList(), context.getSource().getLocation().getWorld());
                })
                .then(Commands.argument("entity-type", VanillaArgumentProviderImpl.resourceOrTag(Registries.ENTITY_TYPE))
                    .executes(context -> {
                        return listEntities(
                            context.getSource().getSender(),
                            (List<Holder.Reference<EntityType<?>>>) context.getArgument("entity-type", List.class),
                            context.getSource().getLocation().getWorld()
                        );
                    })
                    .then(Commands.argument("world", ArgumentTypes.world())
                        .executes(context -> {
                            return listEntities(
                                context.getSource().getSender(),
                                (List<Holder.Reference<EntityType<?>>>) context.getArgument("entity-type", List.class),
                                context.getArgument("world", World.class)
                            );
                        })
                    )
                )
            );
    }

    /*
     * Ported from MinecraftForge - author: LexManos <LexManos@gmail.com> - License: LGPLv2.1
     */
    private static int listEntities(final CommandSender sender, final List<Holder.Reference<EntityType<?>>> entities, final World bukkitWorld) throws CommandSyntaxException {
        Map<Identifier, MutablePair<Integer, Map<ChunkPos, Integer>>> list = new HashMap<>();
        ServerLevel world = ((CraftWorld) bukkitWorld).getHandle();
        Map<Identifier, Integer> nonEntityTicking = new HashMap<>();
        ServerChunkCache chunkProviderServer = world.getChunkSource();
        world.getAllEntities().forEach(e -> {
            Identifier key = EntityType.getKey(e.getType());

            MutablePair<Integer, Map<ChunkPos, Integer>> info = list.computeIfAbsent(key, k -> MutablePair.of(0, new HashMap<>()));
            ChunkPos chunk = e.chunkPosition();
            info.left++;
            info.right.put(chunk, info.right.getOrDefault(chunk, 0) + 1);
            if (!world.isPositionEntityTicking(e.blockPosition()) || (e instanceof net.minecraft.world.entity.Marker && !world.paperConfig().entities.markers.tick)) { // Paper - Configurable marker ticking
                nonEntityTicking.merge(key, 1, Integer::sum);
            }
        });
        if (entities.size() == 1) {
            Identifier name = entities.getFirst().unwrapKey().orElseThrow().identifier();
            Pair<Integer, Map<ChunkPos, Integer>> info = list.get(name);
            int nonTicking = nonEntityTicking.getOrDefault(name, 0);
            if (info == null) {
                throw EntityArgument.NO_ENTITIES_FOUND.create();
            }
            sender.sendPlainMessage("Entity: " + name + " Total Ticking: " + (info.getLeft() - nonTicking) + ", Total Non-Ticking: " + nonTicking);
            List<Map.Entry<ChunkPos, Integer>> entitiesPerChunk = info.getRight().entrySet().stream()
                .sorted((a, b) -> !a.getValue().equals(b.getValue()) ? b.getValue() - a.getValue() : a.getKey().toString().compareTo(b.getKey().toString()))
                .limit(10).toList();
            entitiesPerChunk.forEach(e -> {
                    final int x = (e.getKey().x << 4) + 8;
                    final int z = (e.getKey().z << 4) + 8;
                    final Component message = text("  " + e.getValue() + ": " + e.getKey().x + ", " + e.getKey().z + (chunkProviderServer.isPositionTicking(e.getKey().toLong()) ? " (Ticking)" : " (Non-Ticking)"))
                        .hoverEvent(text("Click to teleport to chunk", GREEN))
                        .clickEvent(ClickEvent.runCommand("/minecraft:execute in " + world.getWorld().getKey() + " run tp " + x + " ~ " + z));
                    sender.sendMessage(message);
                });
            return entitiesPerChunk.size();
        } else {
            List<Identifier> names = entities.stream().map(type -> type.unwrapKey().orElseThrow().identifier()).toList();
            List<Pair<Identifier, Integer>> info = list.entrySet().stream()
                .filter(e -> names.contains(e.getKey()))
                .map(e -> Pair.of(e.getKey(), e.getValue().left))
                .sorted((a, b) -> !a.getRight().equals(b.getRight()) ? b.getRight() - a.getRight() : a.getKey().toString().compareTo(b.getKey().toString()))
                .toList();

            if (info.isEmpty()) {
                throw EntityArgument.NO_ENTITIES_FOUND.create();
            }

            int count = info.stream().mapToInt(Pair::getRight).sum();
            int nonTickingCount = nonEntityTicking.values().stream().mapToInt(Integer::intValue).sum();
            sender.sendPlainMessage("Total Ticking: " + (count - nonTickingCount) + ", Total Non-Ticking: " + nonTickingCount);
            info.forEach(e -> {
                int nonTicking = nonEntityTicking.getOrDefault(e.getKey(), 0);
                sender.sendMessage(text("  " + (e.getValue() - nonTicking) + " (" + nonTicking + ") " + ": " + e.getKey())
                    .hoverEvent(text("Click to inspect " + e.getKey(), GREEN))
                    .clickEvent(ClickEvent.suggestCommand("/paper:paper entity list " + e.getKey())));
            });
            sender.sendPlainMessage("* First number is ticking entities, second number is non-ticking entities");
            return info.size();
        }
    }
}
