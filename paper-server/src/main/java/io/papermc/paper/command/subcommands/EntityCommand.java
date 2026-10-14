package io.papermc.paper.command.subcommands;

import com.google.common.collect.Maps;
import io.papermc.paper.FeatureHooks;
import io.papermc.paper.command.CommandUtil;
import io.papermc.paper.command.PaperSubcommand;
import io.papermc.paper.util.MCUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@DefaultQualifier(NonNull.class)
public final class EntityCommand implements PaperSubcommand {
    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        this.listEntities(sender, args);
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String subCommand, final String[] args) {
        if (args.length == 1) {
            return CommandUtil.getListMatchingLast(sender, args, "help", "list");
        } else if (args.length > 1 && args[0].toLowerCase(Locale.ROOT).equals("list")) {
            if (args.length == 2) {
                return CommandUtil.getListMatchingLast(sender, args, BuiltInRegistries.ENTITY_TYPE.keySet());
            } else if (args.length == 3) {
                return CommandUtil.getListMatchingLast(sender, args, CommandUtil.getWorldSuggestions(sender.getServer(), false));
            }
        }
        return Collections.emptyList();
    }

    /*
     * Ported from MinecraftForge - author: LexManos <LexManos@gmail.com> - License: LGPLv2.1
     */
    private void listEntities(final CommandSender sender, final String[] args) {
        // help
        if (args.length < 1 || !args[0].toLowerCase(Locale.ROOT).equals("list")) {
            sender.sendMessage(text("Use /paper entity list [filter] [world] to get entity info that matches the optional filter.", RED));
            return;
        }

        if ("list".equals(args[0].toLowerCase(Locale.ROOT))) {
            String filter = "*";
            if (args.length > 1) {
                filter = args[1];
            }
            final String cleanFilter = filter.replace("?", ".?").replace("*", ".*?");
            Set<Identifier> names = BuiltInRegistries.ENTITY_TYPE.keySet().stream()
                .filter(n -> n.toString().matches(cleanFilter))
                .collect(Collectors.toSet());
            if (names.isEmpty()) {
                sender.sendMessage(text("Invalid filter, does not match any entities. Use /paper entity list for a proper list", RED));
                sender.sendMessage(text("Usage: /paper entity list [filter] [world]", RED));
                return;
            }

            final @Nullable World world;
            if (args.length > 2) {
                @Nullable NamespacedKey key = NamespacedKey.fromString(args[2]);
                world = key == null ? null : sender.getServer().getWorld(key);
                if (world == null) {
                    sender.sendMessage(text("Could not load world for " + args[2] + ". Please select a valid world.", RED));
                    sender.sendMessage(text("Usage: /paper entity list [filter] [world]", RED));
                    return;
                }
            } else if (sender instanceof Player player) {
                world = player.getWorld();
            } else {
                sender.sendMessage(text("Please specify the key of a world", RED));
                sender.sendMessage(text("To do so without a filter, specify '*' as the filter", RED));
                sender.sendMessage(text("Usage: /paper entity list [filter] [world]", RED));
                return;
            }

            Map<Identifier, MutablePair<Integer, Map<ChunkPos, Integer>>> list = new HashMap<>();
            ServerLevel level = ((CraftWorld) world).getHandle();
            Map<Identifier, Integer> nonEntityTicking = new HashMap<>();
            ServerChunkCache chunkProviderServer = level.getChunkSource();
            FeatureHooks.getAllEntities(level).forEach(e -> {
                Identifier key = EntityType.getKey(e.getType());

                MutablePair<Integer, Map<ChunkPos, Integer>> info = list.computeIfAbsent(key, k -> MutablePair.of(0, Maps.newHashMap()));
                ChunkPos chunk = e.chunkPosition();
                info.left++;
                info.right.put(chunk, info.right.getOrDefault(chunk, 0) + 1);
                if (!level.isPositionEntityTicking(e.blockPosition()) || (e instanceof net.minecraft.world.entity.Marker && !level.paperConfig().entities.markers.tick)) { // Paper - Configurable marker ticking
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
                        final int x = (e.getKey().x() << 4) + 8;
                        final int z = (e.getKey().z() << 4) + 8;
                        final Component message = text("  " + e.getValue() + ": " + e.getKey().x() + ", " + e.getKey().z() + (chunkProviderServer.isPositionTicking(e.getKey().pack()) ? " (Ticking)" : " (Non-Ticking)"))
                            .hoverEvent(HoverEvent.showText(text("Click to teleport to chunk", GREEN)))
                            .clickEvent(ClickEvent.runCommand("/minecraft:execute as @s in " + MCUtil.getLevelName(level) + " run tp " + x + " " + (level.getWorld().getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING) + 1) + " " + z));
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
}
