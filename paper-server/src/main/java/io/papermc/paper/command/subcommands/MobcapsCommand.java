package io.papermc.paper.command.subcommands;

import com.google.common.collect.ImmutableMap;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
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
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MobcapsCommand {

    static final Map<MobCategory, TextColor> MOB_CATEGORY_COLORS = ImmutableMap.<MobCategory, TextColor>builder()
        .put(MobCategory.MONSTER, NamedTextColor.RED)
        .put(MobCategory.CREATURE, NamedTextColor.GREEN)
        .put(MobCategory.AMBIENT, NamedTextColor.GRAY)
        .put(MobCategory.AXOLOTLS, TextColor.color(0x7324FF))
        .put(MobCategory.UNDERGROUND_WATER_CREATURE, TextColor.color(0x3541E6))
        .put(MobCategory.WATER_CREATURE, TextColor.color(0x006EFF))
        .put(MobCategory.WATER_AMBIENT, TextColor.color(0x00B3FF))
        .put(MobCategory.MISC, TextColor.color(0x636363))
        .build();

    private static final SimpleCommandExceptionType REQUIRES_PLAYER = new SimpleCommandExceptionType(MessageComponentSerializer.message().serialize(
        Component.text("Must specify a player! ex: '/paper playermobcount playerName'")
    ));
    private static final DynamicCommandExceptionType NO_SUCH_WORLD_ERROR = new DynamicCommandExceptionType(obj -> MessageComponentSerializer.message().serialize(
        Component.text("'" + obj + "' is not a valid world!")
    ));

    public static LiteralArgumentBuilder<CommandSourceStack> createGlobal() {
        return Commands.literal("mobcaps")
            .requires(stack -> stack.getSender().hasPermission(PaperCommand.BASE_PERM + "mobcaps"))

            .then(Commands.literal("*")
                .executes(ctx -> {
                    printMobcaps(ctx.getSource().getSender(), Bukkit.getWorlds());
                    return Command.SINGLE_SUCCESS;
                })
            )

            .then(Commands.argument("world", ArgumentTypes.world())
                .executes(ctx -> {
                    printMobcaps(ctx.getSource().getSender(), List.of(ctx.getArgument("world", World.class)));
                    return Command.SINGLE_SUCCESS;
                })
            )

            .then(Commands.argument("world_name", StringArgumentType.string())
                .executes(ctx -> {
                    String worldName = StringArgumentType.getString(ctx, "world_name");
                    World world = Bukkit.getWorld(worldName);

                    if (world == null) {
                        throw NO_SUCH_WORLD_ERROR.create(worldName);
                    }

                    printMobcaps(ctx.getSource().getSender(), List.of(world));
                    return Command.SINGLE_SUCCESS;
                })
            )

            .executes(ctx -> {
                printMobcaps(ctx.getSource().getSender(), List.of(ctx.getSource().getLocation().getWorld()));
                return Command.SINGLE_SUCCESS;
            });
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createPlayer() {
        return Commands.literal("playermobcaps")
            .requires(stack -> stack.getSender().hasPermission(PaperCommand.BASE_PERM + "mobcaps"))

            .then(Commands.argument("players", ArgumentTypes.player())
                .executes(ctx -> {
                    ctx.getArgument("players", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).forEach(
                        player -> printPlayerMobcaps(ctx.getSource().getSender(), player)
                    );
                    return Command.SINGLE_SUCCESS;
                })
            )

            .executes(ctx -> {
                if (ctx.getSource().getExecutor() instanceof Player player) {
                    printPlayerMobcaps(ctx.getSource().getSender(), player);
                    return Command.SINGLE_SUCCESS;
                }

                throw REQUIRES_PLAYER.create();
            });
    }

    private static void printMobcaps(final CommandSender sender, final List<World> worlds) {
        for (final World world : worlds) {
            final ServerLevel level = ((CraftWorld) world).getHandle();
            final NaturalSpawner.SpawnState state = level.getChunkSource().getLastSpawnState();

            final int chunks;
            if (state == null) {
                chunks = 0;
            } else {
                chunks = state.getSpawnableChunkCount();
            }
            sender.sendMessage(Component.join(JoinConfiguration.noSeparators(),
                Component.text("Mobcaps for world: "),
                Component.text(world.getName(), NamedTextColor.AQUA),
                Component.text(" (" + chunks + " spawnable chunks)")
            ));

            sender.sendMessage(createMobcapsComponent(
                category -> {
                    if (state == null) {
                        return 0;
                    } else {
                        return state.getMobCategoryCounts().getOrDefault(category, 0);
                    }
                },
                category -> NaturalSpawner.globalLimitForCategory(level, category, chunks)
            ));
        }
    }

    private static void printPlayerMobcaps(final CommandSender sender, final Player player) {
        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        final ServerLevel level = serverPlayer.level();

        if (!level.paperConfig().entities.spawning.perPlayerMobSpawns) {
            sender.sendMessage(Component.text("Use '/paper mobcaps' for worlds where per-player mob spawning is disabled.", NamedTextColor.RED));
            return;
        }

        sender.sendMessage(Component.join(JoinConfiguration.noSeparators(), Component.text("Mobcaps for player: "), Component.text(player.getName(), NamedTextColor.GREEN)));
        sender.sendMessage(createMobcapsComponent(
            category -> level.chunkSource.chunkMap.getMobCountNear(serverPlayer, category),
            category -> level.getWorld().getSpawnLimitUnsafe(org.bukkit.craftbukkit.util.CraftSpawnCategory.toBukkit(category))
        ));
    }

    private static Component createMobcapsComponent(final ToIntFunction<MobCategory> countGetter, final ToIntFunction<MobCategory> limitGetter) {
        return MOB_CATEGORY_COLORS.entrySet().stream()
            .map(entry -> {
                final MobCategory category = entry.getKey();
                final TextColor color = entry.getValue();

                final Component categoryHover = Component.join(JoinConfiguration.noSeparators(),
                    Component.text("Entity types in category ", TextColor.color(0xE0E0E0)),
                    Component.text(category.getName(), color),
                    Component.text(':', NamedTextColor.GRAY),
                    Component.newline(),
                    Component.newline(),
                    BuiltInRegistries.ENTITY_TYPE.entrySet().stream()
                        .filter(it -> it.getValue().getCategory() == category)
                        .map(it -> Component.translatable(it.getValue().getDescriptionId()))
                        .collect(Component.toComponent(Component.text(", ", NamedTextColor.GRAY)))
                );

                final Component categoryComponent = Component.text()
                    .content("  " + category.getName())
                    .color(color)
                    .hoverEvent(categoryHover)
                    .build();

                final TextComponent.Builder builder = Component.text()
                    .append(
                        categoryComponent,
                        Component.text(": ", NamedTextColor.GRAY)
                    );
                final int limit = limitGetter.applyAsInt(category);
                if (limit != -1) {
                    builder.append(
                        Component.text(countGetter.applyAsInt(category)),
                        Component.text("/", NamedTextColor.GRAY),
                        Component.text(limit)
                    );
                } else {
                    builder.append(Component.text()
                        .append(
                            Component.text('n'),
                            Component.text("/", NamedTextColor.GRAY),
                            Component.text('a')
                        )
                        .hoverEvent(Component.text("This category does not naturally spawn.")));
                }
                return builder;
            })
            .map(ComponentLike::asComponent)
            .collect(Component.toComponent(Component.newline()));
    }
}
