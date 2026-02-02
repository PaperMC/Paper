package io.papermc.paper.command.subcommands;

import com.google.common.collect.Maps;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;

public final class MobcapsCommand {

    static final Map<MobCategory, TextColor> MOB_CATEGORY_COLORS = Maps.immutableEnumMap(Util.make(new EnumMap<>(MobCategory.class), map -> {
        map.put(MobCategory.MONSTER, NamedTextColor.RED);
        map.put(MobCategory.CREATURE, NamedTextColor.GREEN);
        map.put(MobCategory.AMBIENT, NamedTextColor.GRAY);
        map.put(MobCategory.AXOLOTLS, TextColor.color(0x7324FF));
        map.put(MobCategory.UNDERGROUND_WATER_CREATURE, TextColor.color(0x3541E6));
        map.put(MobCategory.WATER_CREATURE, TextColor.color(0x006EFF));
        map.put(MobCategory.WATER_AMBIENT, TextColor.color(0x00B3FF));
        map.put(MobCategory.MISC, TextColor.color(0x636363));
    }));

    public static LiteralArgumentBuilder<CommandSourceStack> createGlobal() {
        return Commands.literal("mobcaps")
            .requires(PaperCommand.hasPermission("mobcaps"))
            .executes(context -> {
                return printMobcaps(context.getSource().getSender(), List.of(context.getSource().getLocation().getWorld()));
            })
            .then(Commands.literal("*")
                .executes(context -> {
                    return printMobcaps(context.getSource().getSender(), Bukkit.getWorlds());
                })
            )
            .then(Commands.argument("world", ArgumentTypes.world())
                .executes(context -> {
                    return printMobcaps(context.getSource().getSender(), List.of(context.getArgument("world", World.class)));
                })
            );
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createPlayer() {
        return Commands.literal("playermobcaps")
            .requires(PaperCommand.hasPermission("playermobcaps"))
            .executes(context -> {
                if (!(context.getSource().getExecutor() instanceof Player player)) {
                    throw net.minecraft.commands.CommandSourceStack.ERROR_NOT_PLAYER.create();
                }
                return printPlayerMobcaps(context.getSource().getSender(), player);
            })
            .then(Commands.argument("player", ArgumentTypes.player())
                .executes(context -> {
                    return printPlayerMobcaps(
                        context.getSource().getSender(),
                        context.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(context.getSource()).getFirst()
                    );
                })
            );
    }

    private static int printMobcaps(final CommandSender sender, final List<World> worlds) {
        for (final World world : worlds) {
            final ServerLevel level = ((CraftWorld) world).getHandle();
            final NaturalSpawner.SpawnState state = level.getChunkSource().getLastSpawnState();

            final int chunks = state == null ? 0 : state.getSpawnableChunkCount();
            sender.sendMessage(Component.join(JoinConfiguration.noSeparators(),
                text("Mobcaps for world: "),
                text(world.key().asString(), NamedTextColor.AQUA),
                text(" (" + chunks + " spawnable chunks)")
            ));

            sender.sendMessage(createMobcapsComponent(
                category -> {
                    return state == null ? 0 : state.getMobCategoryCounts().getOrDefault(category, 0);
                },
                category -> NaturalSpawner.globalLimitForCategory(level, category, chunks)
            ));
        }
        return worlds.size();
    }

    private static int printPlayerMobcaps(final CommandSender sender, final Player player) {
        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        final ServerLevel level = serverPlayer.level();

        if (!level.paperConfig().entities.spawning.perPlayerMobSpawns) {
            sender.sendMessage(text("Use '/paper mobcaps' for worlds where per-player mob spawning is disabled.", NamedTextColor.RED));
            return 0;
        }

        sender.sendMessage(Component.join(JoinConfiguration.noSeparators(), text("Mobcaps for player: "), text(player.getName(), NamedTextColor.GREEN)));
        sender.sendMessage(createMobcapsComponent(
            category -> level.chunkSource.chunkMap.getMobCountNear(serverPlayer, category),
            category -> level.getWorld().getSpawnLimitUnsafe(org.bukkit.craftbukkit.util.CraftSpawnCategory.toBukkit(category))
        ));
        return Command.SINGLE_SUCCESS;
    }

    private static Component createMobcapsComponent(final ToIntFunction<MobCategory> countGetter, final ToIntFunction<MobCategory> limitGetter) {
        return MOB_CATEGORY_COLORS.entrySet().stream()
            .map(entry -> {
                final MobCategory category = entry.getKey();
                final TextColor color = entry.getValue();

                final Component categoryHover = Component.join(JoinConfiguration.noSeparators(),
                    text("Entity types in category ", TextColor.color(0xE0E0E0)),
                    text(category.getName(), color),
                    text(':', NamedTextColor.GRAY),
                    Component.newline(),
                    Component.newline(),
                    BuiltInRegistries.ENTITY_TYPE.entrySet().stream()
                        .filter(it -> it.getValue().getCategory() == category)
                        .map(it -> Component.translatable(it.getValue().getDescriptionId()))
                        .collect(Component.toComponent(text(", ", NamedTextColor.GRAY)))
                );

                final Component categoryComponent = text()
                    .content("  " + category.getName())
                    .color(color)
                    .hoverEvent(categoryHover)
                    .build();

                final TextComponent.Builder builder = text()
                    .append(
                        categoryComponent,
                        text(": ", NamedTextColor.GRAY)
                    );
                final int limit = limitGetter.applyAsInt(category);
                if (limit != -1) {
                    builder.append(
                        text(countGetter.applyAsInt(category)),
                        text("/", NamedTextColor.GRAY),
                        text(limit)
                    );
                } else {
                    builder.append(text()
                        .append(
                            text('n'),
                            text("/", NamedTextColor.GRAY),
                            text('a')
                        )
                        .hoverEvent(text("This category does not naturally spawn.")));
                }
                return builder;
            })
            .map(ComponentLike::asComponent)
            .collect(Component.toComponent(Component.newline()));
    }
}
