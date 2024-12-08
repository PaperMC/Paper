package io.papermc.testplugin;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.block.LidMode;
import io.papermc.paper.block.LidState;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lidded;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public final class TestPlugin extends JavaPlugin implements Listener {

    private final Dynamic3CommandExceptionType NOT_LIDDED = new Dynamic3CommandExceptionType(
        (objPos, objExpected, objActual) -> {
            if (!(objPos instanceof BlockPosition pos) || !(objExpected instanceof String expected)
                || !(objActual instanceof String actual)) {
                getLogger().warning(
                    "Internal Exception while making error message" + objPos.getClass()
                        + objExpected.getClass() + objActual.getClass());
                return new LiteralMessage("Internal Exception while making error message");
            }

            return new LiteralMessage(
                "Block at %d, %d, %d is not a lidded block. Expected: \"%s\", Actual: \"%s\"".formatted(
                    pos.blockX(), pos.blockY(), pos.blockZ(), expected, actual));
        });

    private io.papermc.paper.block.Lidded getPaperLidded(
        CommandContext<io.papermc.paper.command.brigadier.CommandSourceStack> context,
        BlockPosition pos
    )
        throws CommandSyntaxException {

        Location targetLoc = new Location(context.getSource().getLocation().getWorld(),
            pos.blockX(), pos.blockY(), pos.blockZ());
        BlockState state = targetLoc.getBlock().getState();
        if (state instanceof io.papermc.paper.block.Lidded lidded) {
            return lidded;
        }
        throw NOT_LIDDED.create(pos, "A block Implementing Paper Lidded",
            state.getType().key().asString());
    }

    private Lidded getBukkitLidded(
        CommandContext<io.papermc.paper.command.brigadier.CommandSourceStack> context,
        BlockPosition pos
    ) throws CommandSyntaxException {
        Location targetLoc = new Location(context.getSource().getLocation().getWorld(),
            pos.blockX(), pos.blockY(), pos.blockZ());
        BlockState state = targetLoc.getBlock().getState();
        if (state instanceof Lidded lidded) {
            return lidded;
        }
        throw NOT_LIDDED.create(pos, "A block Implementing Bukkit Lidded",
            state.getType().key().asString());
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        LifecycleEventManager<Plugin> lifecycleManager = this.getLifecycleManager();

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(
                Commands.literal("test_lidded_new")
                    .then(Commands.argument("pos", ArgumentTypes.blockPosition())
                        .then(Commands.literal("query")
                            .executes(context -> {
                                BlockPosition pos = context.getArgument("pos",
                                    BlockPositionResolver.class).resolve(context.getSource());
                                io.papermc.paper.block.Lidded lidded = getPaperLidded(context, pos);
                                if (!(lidded instanceof BlockState state)) {
                                    throw new IllegalStateException("Impossible");
                                }
                                Component msg = text()
                                    .append(text("---", NamedTextColor.DARK_GRAY)).appendNewline()
                                    .append(text("Lidded Block: "))
                                    .append(text(state.getType().key().asString())).appendNewline()
                                    .append(text("Lid Mode: "))
                                    .append(text(lidded.getLidMode().name())).appendNewline()
                                    .append(text("Effective LidState: "))
                                    .append(text(lidded.getEffectiveLidState().name(), lidded.getEffectiveLidState() == LidState.OPEN ? NamedTextColor.GREEN : NamedTextColor.RED)).appendSpace()
                                    .append(text("True LidState: "))
                                    .append(text(lidded.getTrueLidState().name(), lidded.getTrueLidState() == LidState.OPEN ? NamedTextColor.GREEN : NamedTextColor.RED))
                                    .build();
                                context.getSource().getSender().sendMessage(msg);
                                return 1;
                            })
                        )
                        .then(Commands.literal("set")
                            .then(Commands.argument("lidMode", LidModeArgument.lidMode())
                                .executes(context -> {
                                        BlockPosition pos = context.getArgument("pos",
                                            BlockPositionResolver.class).resolve(context.getSource());
                                        io.papermc.paper.block.Lidded lidded = getPaperLidded(context, pos);
                                        if (!(lidded instanceof BlockState state)) {
                                            throw new IllegalStateException("Impossible");
                                        }

                                        LidMode previousMode = lidded.getLidMode();
                                        LidState oldEffectiveState = lidded.getEffectiveLidState();
                                        LidState oldTrueState = lidded.getTrueLidState();
                                        LidMode targetMode = LidModeArgument.getLidMode(context, "lidMode");
                                        LidMode resultantMode = lidded.setLidMode(targetMode);

                                        Component msg = text()
                                            .append(text("---", NamedTextColor.DARK_GRAY)).appendNewline()
                                            .append(text("Lidded Block: "))
                                            .append(text(state.getType().key().asString())).appendNewline()
                                            .append(text("Lid Mode: "))
                                            .append(text("Old: "))
                                            .append(text(previousMode.name())).appendSpace()
                                            .append(text("Target: "))
                                            .append(text(targetMode.name())).appendSpace()
                                            .append(text("New: "))
                                            .append(text(resultantMode.name())).appendNewline()
                                            .append(text("Effective LidState: "))
                                            .append(text("Old: "))
                                            .append(text(oldEffectiveState.name(), oldEffectiveState == LidState.OPEN ? NamedTextColor.GREEN : NamedTextColor.RED)).appendSpace()
                                            .append(text("New: "))
                                            .append(text(lidded.getEffectiveLidState().name(), lidded.getEffectiveLidState() == LidState.OPEN ? NamedTextColor.GREEN : NamedTextColor.RED)).appendNewline()
                                            .append(text("True LidState: "))
                                            .append(text("Old: "))
                                            .append(text(oldTrueState.name(), oldTrueState == LidState.OPEN ? NamedTextColor.GREEN : NamedTextColor.RED)).appendSpace()
                                            .append(text("New: "))
                                            .append(text(lidded.getTrueLidState().name(), lidded.getTrueLidState() == LidState.OPEN ? NamedTextColor.GREEN : NamedTextColor.RED))
                                            .build();
                                        context.getSource().getSender().sendMessage(msg);


                                        return 1;
                                    }
                                )
                            )
                        )
                    )
                    .build()
            );
            event.registrar().register(
                Commands.literal("test_lidded_old")
                    .then(Commands.argument("pos", ArgumentTypes.blockPosition())
                        .then(Commands.literal("is_open")
                            .executes(context -> {
                                BlockPosition pos = context.getArgument("pos",
                                    BlockPositionResolver.class).resolve(context.getSource());
                                Lidded lidded = getBukkitLidded(context, pos);
                                if (!(lidded instanceof BlockState state)) {
                                    throw new IllegalStateException("Impossible");
                                }

                                Component msg = text()
                                    .append(text("Lidded Block: "))
                                    .append(text(state.getType().key().asString()))
                                    .append(text(" is open: "))
                                    .append(text(lidded.isOpen() ? "Yes" : "No", lidded.isOpen() ? NamedTextColor.GREEN : NamedTextColor.RED))
                                    .build();

                                context.getSource().getSender().sendMessage(msg);


                                return 1;
                            })
                        )
                        .then(Commands.literal("open")
                            .executes(context -> {
                                BlockPosition pos = context.getArgument("pos",
                                    BlockPositionResolver.class).resolve(context.getSource());
                                Lidded lidded = getBukkitLidded(context, pos);
                                if (!(lidded instanceof BlockState state)) {
                                    throw new IllegalStateException("Impossible");
                                }

                                lidded.open();

                                Component msg = text()
                                    .append(text("Lidded Block: "))
                                    .append(text(state.getType().key().asString()))
                                    .append(text(" set "))
                                    .append(text("open", NamedTextColor.GREEN))
                                    .build();

                                context.getSource().getSender().sendMessage(msg);

                                return 1;
                            })
                        )
                        .then(Commands.literal("close")
                            .executes(context -> {
                                BlockPosition pos = context.getArgument("pos",
                                    BlockPositionResolver.class).resolve(context.getSource());
                                Lidded lidded = getBukkitLidded(context, pos);
                                if (!(lidded instanceof BlockState state)) {
                                    throw new IllegalStateException("Impossible");
                                }

                                lidded.close();
                                Component msg = text()
                                    .append(text("Lidded Block: "))
                                    .append(text(state.getType().key().asString()))
                                    .append(text(" set "))
                                    .append(text("closed", NamedTextColor.RED))
                                    .build();

                                context.getSource().getSender().sendMessage(msg);

                                return 1;
                            })
                        )
                    )
                    .build()
            );


        });

        getServer().getPluginManager().registerEvents(new LiddedTestListener(this.getLogger()), this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    public static class LidModeArgument implements CustomArgumentType.Converted<LidMode, String> {

        private static final SimpleCommandExceptionType INVALID_MODE = new SimpleCommandExceptionType(
            new LiteralMessage("Invalid lid mode"));

        private static final LinkedHashMap<String, LidMode> BY_STRING = Arrays.stream(
                LidMode.values())
            .collect(
                Collectors.toMap(LidMode::name, Function.identity(), (lidMode, lidMode2) -> lidMode,
                    LinkedHashMap::new));

        static boolean matchesSubStr(String remaining, String candidate) {
            for (int i = 0; !candidate.startsWith(remaining, i); i++) {
                int j = candidate.indexOf(46, i);
                int k = candidate.indexOf(95, i);
                if (Math.max(j, k) < 0) {
                    return false;
                }

                if (j >= 0 && k >= 0) {
                    i = Math.min(k, j);
                } else {
                    i = j >= 0 ? j : k;
                }
            }

            return true;
        }

        public static LidModeArgument lidMode() {
            return new LidModeArgument();
        }

        public static LidMode getLidMode(CommandContext<?> context, String name) {
            return context.getArgument(name, LidMode.class);
        }

        @Override
        public @NotNull LidMode convert(@NotNull final String nativeType)
            throws CommandSyntaxException {
            LidMode mode = BY_STRING.get(nativeType.toUpperCase(Locale.ROOT));
            if (mode == null) {
                throw INVALID_MODE.create();
            }
            return mode;
        }

        @Override
        public @NotNull ArgumentType<String> getNativeType() {
            return StringArgumentType.word();
        }

        @Override
        public @NotNull Collection<String> getExamples() {
            return BY_STRING.keySet();
        }

        @Override
        public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(
            final @NotNull CommandContext<S> context, final @NotNull SuggestionsBuilder builder
        ) {

            String string = builder.getRemaining().toLowerCase(Locale.ROOT);
            BY_STRING.keySet().stream()
                .filter(candidate -> matchesSubStr(string, candidate.toLowerCase(Locale.ROOT)))
                .forEach(builder::suggest);
            return builder.buildFuture();
        }
    }


}
